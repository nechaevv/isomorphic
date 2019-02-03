package com.github.nechaevv.isomorphic.dom

import cats.effect.IO
import com.github.nechaevv.isomorphic.frontend.DOMEventType
import org.scalajs.dom.Event

object browser {
  type EventHandler = Event ⇒ fs2.Stream[IO, Any]

  implicit class PimpedEventType(eventType: DOMEventType) {
    def → (handler: EventHandler): NodeEventListener = NodeEventListener(eventType, handler)
  }
}
