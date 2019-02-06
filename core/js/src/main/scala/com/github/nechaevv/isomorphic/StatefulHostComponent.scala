package com.github.nechaevv.isomorphic

import cats.effect.{Concurrent, ContextShift, IO}
import com.github.nechaevv.isomorphic.api.{HTMLElementWithShadowRoot, ReactDOM}
import com.github.nechaevv.isomorphic.vdom.{ComponentVNode, DomReconciler, FragmentVNode}
import fs2.Stream
import fs2.concurrent.Queue
import org.scalajs.dom.raw.HTMLElement

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js
import scala.util.control.NonFatal

trait StatefulHostComponent {
  type State <: AnyRef

  def attributes: Iterable[String] = Seq.empty
  def initialState(properties: Iterable[(String, String)]): State
  def reducer: Any ⇒ State ⇒ State = _ ⇒ s ⇒ s
  def effect: Any ⇒ State ⇒ EventStream = _ ⇒ _ ⇒ fs2.Stream.empty
  def render(componentHost: HTMLElement): EventDispatcher ⇒ State ⇒ Unit

  def onStart(componentHost: HTMLElement, eventDispatcher: EventDispatcher): Unit = ()
  def onStop(componentHost: HTMLElement): Unit = ()

  def eventReducerPipeline(componentHost: HTMLElement, eventDispatcher: EventDispatcher)
                          (implicit concurrent: Concurrent[IO]): IO[Unit] = {
    val initState = initialState(attributes.map(attribute ⇒ attribute → componentHost.getAttribute(attribute)))
    val renderFn = render(componentHost)(eventDispatcher)
    val events = eventDispatcher.dequeue.takeWhile(event ⇒ event != ComponentDisconnectedEvent, takeFailure = true)
    onStart(componentHost, eventDispatcher)
    (for {
      reducerOutput ← events.scan[(State, Any, Boolean)]((initState, ComponentConnectedEvent, true))((acc, event: Any) ⇒ {
        val (state, _, _) = acc
        if (event == ComponentConnectedEvent) acc
        else {
          val newState = try {
            reducer(event)(state)
          } catch {
            case NonFatal(err) ⇒
              err.printStackTrace()
              state
          }
          (newState, event, !(state eq newState))
        }
      })
      (state, event, hasChanged) = reducerOutput
      renderStream = if (hasChanged) Stream.eval(IO {
        println(s"Tick, event: $event")
        renderFn(state)
      }) else Stream.empty
      effectStream = Stream.eval(concurrent.start(
        effect(event)(state).through(eventDispatcher.enqueue).compile.drain
          .handleErrorWith(err ⇒ IO(err.printStackTrace()))
      ))
      _ ← renderStream ++ effectStream
    } yield ()).compile.drain
      .map(_ ⇒ onStop(componentHost))
  }

}

case object ComponentConnectedEvent
case object ComponentDisconnectedEvent
case object ComponentAdoptedEvent
case class ComponentAttributeChangedEvent(name: String, oldValue: String, newValue: String)

abstract class StatefulHostCustomElement[T <: StatefulHostComponent](val webComponent: T) extends HTMLElement {
  implicit val defaultContextShift: ContextShift[IO] = IO.contextShift(global)

  private var eventDispatcherOpt: Option[EventDispatcher] = None
  private def sendEvent(event: Any): Unit = eventDispatcherOpt.foreach(_.enqueue1(event).unsafeRunSync())

  def connectedCallback(): Unit = (for {
    eventStream ← Queue.unbounded[IO, Any]
    _ ← webComponent.eventReducerPipeline(this, eventStream)
  } yield {
    eventDispatcherOpt = Some(eventStream)
  }).unsafeRunAsyncAndForget()

  def disconnectedCallback(): Unit = {
    sendEvent(ComponentDisconnectedEvent)
    eventDispatcherOpt = None
  }

  def adoptedCallback(): Unit = sendEvent(ComponentAdoptedEvent)

  def attributeChangedCallback(name: String, oldValue: String, newValue: String): Unit = {
    sendEvent(ComponentAttributeChangedEvent(name, oldValue, newValue))
  }

}

trait ReactRender { this: StatefulHostComponent ⇒
  def rootComponent: State ⇒ Element

  override def render(componentHost: HTMLElement): EventDispatcher ⇒ State ⇒ Unit = {
    eventDispatcher: EventDispatcher ⇒ state: State ⇒ {
      val vdom = rootComponent(state)(new ReactRenderer(eventDispatcher))
      ReactDOM.render(vdom, componentHost)
    }
  }
}

trait DomReconcilerRender { this: StatefulHostComponent ⇒
  def rootComponent: vdom.Component[State, FragmentVNode]

  override def render(componentHost: HTMLElement): EventDispatcher ⇒ State ⇒ Unit = {
    eventDispatcher ⇒ state ⇒ {
      DomReconciler.reconcileRootComponent(componentHost, ComponentVNode(rootComponent, state), eventDispatcher)
    }
  }
}

trait ShadowDomReconcilerRender { this: StatefulHostComponent ⇒
  def rootComponent: vdom.Component[State, FragmentVNode]
  def isOpen: Boolean = false
  override def render(componentHost: HTMLElement): EventDispatcher ⇒ State ⇒ Unit = {
    val shadowRoot = componentHost.asInstanceOf[HTMLElementWithShadowRoot]
      .attachShadow(js.Dynamic.literal("mode" → (if (isOpen) "open" else "closed")))
    eventDispatcher ⇒ state ⇒ {
      DomReconciler.reconcileRootComponent(shadowRoot, ComponentVNode(rootComponent, state), eventDispatcher)
    }
  }
}
