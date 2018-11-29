package com.github.nechaevv.isomorphic

import cats.effect.{ContextShift, IO}
import com.github.nechaevv.isomorphic.api.HTMLElementWithShadowRoot
import org.scalajs.dom.raw.{HTMLElement, Node}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js

trait StatefulHostComponent {
  type State <: AnyRef

  def attributes: Iterable[String] = Seq.empty
  def rootComponent: Component[State]
  def initialState(properties: Iterable[(String, String)]): State
  def reducer: Any ⇒ State ⇒ State
  def effect: Any ⇒ State ⇒ EventStream
  def useShadowRoot: Boolean = false

  def connectedEffect: Option[Any] = None
  def adoptedEffect: Option[Any] = None
  def attributeChangedEffect(name: String, oldValue: String, newValue: String): Option[Any] = None

}

class StatefulHostCustomElement[T <: StatefulHostComponent](val webComponent: T) extends HTMLElement {
  implicit val defaultContextShift: ContextShift[IO] = IO.contextShift(global)

  private var eventStream: Option[EventDispatcher] = None
  private def sendEvent(event: Any): Unit = eventStream.foreach(_.enqueue1(event).unsafeRunSync())

  private val container: Node = if (webComponent.useShadowRoot)
    this.asInstanceOf[HTMLElementWithShadowRoot].attachShadow(js.Dynamic.literal("mode" → "open")) else this

  def connectedCallback(): Unit = {
    ReactPipeline.run[webComponent.State](
      container,
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