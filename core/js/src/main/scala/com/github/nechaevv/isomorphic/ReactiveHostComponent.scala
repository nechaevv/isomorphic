package com.github.nechaevv.isomorphic

import cats.effect.{ContextShift, IO}
import com.github.nechaevv.isomorphic.api.HTMLElementWithShadowRoot
import org.scalajs.dom.raw.{HTMLElement, Node}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.scalajs.js

trait ReactiveHostComponent {
  type State <: AnyRef

  def attributes: Iterable[String] = Seq.empty
  def rootComponent: Component[State]
  def initialState(properties: Iterable[(String, String)]): State
  def reducer: Reducer[State]
  def effect: Effect[State]
  def useShadowRoot: Boolean = false

  def connectedEffect: Option[Any] = None
  def adoptedEffect: Option[Any] = None
  def attributeChangedEffect(name: String, oldValue: String, newValue: String): Option[Any] = None

}

class ReactiveHostCustomElement[T <: ReactiveHostComponent](val webComponent: T) extends HTMLElement {
  implicit val defaultContextShift: ContextShift[IO] = IO.contextShift(global)

  private var dispatcher: Option[EventDispatcher] = None

  private val container: Node = if (webComponent.useShadowRoot)
    this.asInstanceOf[HTMLElementWithShadowRoot].attachShadow(js.Dynamic.literal("mode" → "open")) else this

  def connectedCallback(): Unit = ReactPipeline.run[webComponent.State](
    container,
    webComponent.rootComponent,
    webComponent.reducer,
    webComponent.effect,
    webComponent.initialState(webComponent.attributes.map(attribute ⇒ attribute → getAttribute(attribute))),
    eventDispatcher ⇒ this.dispatcher = Some(eventDispatcher)
  ).unsafeRunAsyncAndForget()

  def disconnectedCallback(): Unit = {
    dispatcher.foreach(_.dispatch(AppStopEvent))
    dispatcher = None
  }

  def adoptedCallback(): Unit = for {
    event ← webComponent.adoptedEffect
    dispatcher ← dispatcher
  } dispatcher.dispatch(event)

  def attributeChangedCallback(name: String, oldValue: String, newValue: String): Unit = for {
    event ← webComponent.attributeChangedEffect(name, oldValue, newValue)
    dispatcher ← dispatcher
  } dispatcher.dispatch(event)


}