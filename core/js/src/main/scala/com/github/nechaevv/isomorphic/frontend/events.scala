package com.github.nechaevv.isomorphic.frontend

case class DOMEventType(name: String, isCapturing: Boolean = false)

object DOMEventTypes {
  val Click = DOMEventType("Click")
  val Change = DOMEventType("Change")
}
