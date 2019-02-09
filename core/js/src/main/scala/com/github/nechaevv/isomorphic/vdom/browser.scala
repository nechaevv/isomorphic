package com.github.nechaevv.isomorphic.vdom

import cats.effect.IO
import org.scalajs.dom.Event

object browser {
  type EventHandler = Event ⇒ fs2.Stream[IO, Any]

  implicit class PimpedEventType(eventType: DOMEventType) {
    def → (handler: EventHandler): VNodeEventListener = VNodeEventListener(eventType, handler)
  }
}
