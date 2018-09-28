package com.github.nechaevv.isomorphic.frontend

case class DOMEventType(name: String, isCapturing: Boolean = false)

object EventTypes {
  val Click = DOMEventType("Click")
  val Change = DOMEventType("Change")
}
