package com.github.nechaevv.isomorphic

case class EventType(name: String, isCapturing: Boolean = false)

object EventTypes {
  val Click = EventType("Click")
  val Change = EventType("Change")
}
