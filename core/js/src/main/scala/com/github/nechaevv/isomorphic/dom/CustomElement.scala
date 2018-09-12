package com.github.nechaevv.isomorphic.dom

import cats.effect.internals.IOContextShift
import cats.effect.{ContextShift, IO}
import com.github.nechaevv.isomorphic.{EventDispatcher, ReactPipeline, WebComponent}
import org.scalajs.dom.raw.{HTMLElement, Node}

import scala.scalajs.js

class CustomElement[T <: WebComponent](val webComponent: T) extends HTMLElement {
  implicit val defaultContextShift: ContextShift[IO] = IOContextShift.global

  private var dispatcher: Option[EventDispatcher[webComponent.Event]] = None

  private val container: Node = if (webComponent.useShadowRoot)
    this.asInstanceOf[ElementWithShadowRoot].attachShadow(js.Dynamic.literal("mode" → "open")) else this

  def connectedCallback(): Unit = for {
    event ← webComponent.connectedEffect
    dispatcher ← dispatcher
  } dispatcher.dispatch(event)

  def disconnectedCallback(): Unit = for {
    event ← webComponent.disconnectedEffect
    dispatcher ← dispatcher
  } dispatcher.dispatch(event)

  def adoptedCallback(): Unit = for {
    event ← webComponent.adoptedEffect
    dispatcher ← dispatcher
  } dispatcher.dispatch(event)

  def attributeChangedCallback(name: String, oldValue: String, newValue: String): Unit = for {
    event ← webComponent.attributeChangedEffect(name, oldValue, newValue)
    dispatcher ← dispatcher
  } dispatcher.dispatch(event)

  ReactPipeline.run[webComponent.Event, webComponent.State, webComponent.Component](container,
    webComponent.rootComponent,
    webComponent.reducer,
    webComponent.initialState,
    webComponent.initEvent(webComponent.attributes.map(attribute ⇒ attribute → getAttribute(attribute))),
    eventDispatcher ⇒ this.dispatcher = Some(eventDispatcher)
  ).unsafeRunAsyncAndForget()

}
