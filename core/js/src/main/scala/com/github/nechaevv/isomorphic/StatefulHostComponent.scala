package com.github.nechaevv.isomorphic

import cats.effect.{ContextShift, IO}
import com.github.nechaevv.isomorphic.api.{HTMLElementWithShadowRoot, ReactDOM}
import com.github.nechaevv.isomorphic.dom.{ComponentVNode, DomReconciler, FragmentVNode, ElementVNode}
import org.scalajs.dom.raw.{HTMLElement, Node}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js

trait StatefulHostComponent {
  type State <: AnyRef

  def attributes: Iterable[String] = Seq.empty
  def initialState(properties: Iterable[(String, String)]): State
  def reducer: Any ⇒ State ⇒ State = _ ⇒ s ⇒ s
  def effect: Any ⇒ State ⇒ EventStream = _ ⇒ _ ⇒ fs2.Stream.empty
  def render(componentHost: HTMLElement): EventDispatcher ⇒ State ⇒ Unit

  def connectedEffect: Option[Any] = None
  def adoptedEffect: Option[Any] = None
  def attributeChangedEffect(name: String, oldValue: String, newValue: String): Option[Any] = None

}

abstract class StatefulHostCustomElement[T <: StatefulHostComponent](val webComponent: T) extends HTMLElement {
  implicit val defaultContextShift: ContextShift[IO] = IO.contextShift(global)

  private var eventStream: Option[EventDispatcher] = None
  private def sendEvent(event: Any): Unit = eventStream.foreach(_.enqueue1(event).unsafeRunSync())


  def connectedCallback(): Unit = {
    EventReducerPipeline.run[webComponent.State](
      webComponent.render(this),
      webComponent.reducer,
      webComponent.effect,
      webComponent.initialState(webComponent.attributes.map(attribute ⇒ attribute → getAttribute(attribute))),
    ).map(es ⇒ eventStream = Some(es)).unsafeRunAsyncAndForget()
  }

  def disconnectedCallback(): Unit = {
    sendEvent(AppStopEvent)
    eventStream = None
  }

  def adoptedCallback(): Unit = sendEvent(webComponent.adoptedEffect)

  def attributeChangedCallback(name: String, oldValue: String, newValue: String): Unit = {
    sendEvent(webComponent.attributeChangedEffect(name, oldValue, newValue))
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
  def rootComponent: dom.Component[State, FragmentVNode]

  override def render(componentHost: HTMLElement): EventDispatcher ⇒ State ⇒ Unit = {
    eventDispatcher ⇒ state ⇒ {
      DomReconciler.reconcileRootComponent(componentHost, ComponentVNode(rootComponent, state), eventDispatcher)
    }
  }
}

trait ShadowDomReconcilerRender { this: StatefulHostComponent ⇒
  def rootComponent: dom.Component[State, FragmentVNode]
  def isOpen: Boolean = false
  override def render(componentHost: HTMLElement): EventDispatcher ⇒ State ⇒ Unit = {
    val shadowRoot = componentHost.asInstanceOf[HTMLElementWithShadowRoot]
      .attachShadow(js.Dynamic.literal("mode" → (if (isOpen) "open" else "closed")))
    eventDispatcher ⇒ state ⇒ {
      DomReconciler.reconcileRootComponent(shadowRoot, ComponentVNode(rootComponent, state), eventDispatcher)
    }
  }
}
