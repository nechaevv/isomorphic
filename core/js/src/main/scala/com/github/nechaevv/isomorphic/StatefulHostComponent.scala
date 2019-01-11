package com.github.nechaevv.isomorphic

import cats.effect.{ContextShift, IO}
import com.github.nechaevv.isomorphic.api.{HTMLElementWithShadowRoot, ReactDOM}
import org.scalajs.dom.raw.{HTMLElement, Node}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js

trait StatefulHostComponent {
  type State <: AnyRef

  def attributes: Iterable[String] = Seq.empty
  def rootComponent: Component[State]
  def renderContainer(hostElement: HTMLElement): Node = hostElement
  def initialState(properties: Iterable[(String, String)]): State
  def reducer: Any ⇒ State ⇒ State
  def effect: Any ⇒ State ⇒ EventStream
  def render(componentHost: HTMLElement): (Element, EventDispatcher) ⇒ Unit

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
      webComponent.rootComponent,
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

trait ShadowRoot { this: StatefulHostComponent ⇒
  override def renderContainer(hostElement: HTMLElement): Node = hostElement.asInstanceOf[HTMLElementWithShadowRoot]
    .attachShadow(js.Dynamic.literal("mode" → "open"))
}

trait ReactRender { this: StatefulHostComponent ⇒
  override def render(componentHost: HTMLElement): (Element, EventDispatcher) ⇒ Unit = {
    val container = renderContainer(componentHost)
    (element: Element, eventDispatcher: EventDispatcher) ⇒ {
      val vdom = element(new ReactRenderer(eventDispatcher))
      ReactDOM.render(vdom, container)
    }
  }
}