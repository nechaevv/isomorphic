package com.github.nechaevv.sjsui.dom

import org.scalajs.dom.raw.{HTMLDocument, HTMLElement}

import scala.scalajs.js

@js.native
trait ElementWithShadowRoot extends HTMLElement {
  def attachShadow(init: js.Object): HTMLDocument = js.native
  val shadowRoot: HTMLDocument
}
