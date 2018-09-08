package com.github.nechaevv.core

case class EventType(domName: String, isCapturing: Boolean = false)

object EventTypes {
  val Click = EventType("click")
  val Change = EventType("change")
}
