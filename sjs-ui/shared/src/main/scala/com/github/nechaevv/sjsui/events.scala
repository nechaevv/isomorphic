package com.github.nechaevv.sjsui

case class EventType(name: String, isCapturing: Boolean = false)

object EventTypes {
  val Click = EventType("Click")
  val Change = EventType("Change")
}
