package com.github.nechaevv.isomorphic.dom

import cats.effect.IO
import com.github.nechaevv.isomorphic.frontend.DOMEventType
import org.scalajs.dom.Event

import scala.scalajs.js

object browser {
  type EventHandler = js.Function1[Event, fs2.Stream[IO, Any]]

  implicit class PimpedEventType(eventType: DOMEventType) {
    def → (handler: EventHandler): NodeEventListener = NodeEventListener(eventType, handler)
  }
}
