package com.github.nechaevv.isomorphic.vdom

case class DOMEventType(name: String, isCapturing: Boolean = false)

object DOMEventTypes {
  val Blur   = DOMEventType("Blur")
  val Change = DOMEventType("Change")
  val Click  = DOMEventType("Click")
  val Focus  = DOMEventType("Focus")
  val Input  = DOMEventType("Input")
}
