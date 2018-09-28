package com.github.nechaevv.isomorphic.api

import org.scalajs.dom.raw.{HTMLDocument, HTMLElement}

import scala.scalajs.js

@js.native
trait HTMLElementWithShadowRoot extends HTMLElement {
  def attachShadow(init: js.Object): HTMLDocument = js.native
  val shadowRoot: HTMLDocument
}
