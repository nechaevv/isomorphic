package com.github.nechaevv.isomorphic

import cats.effect.IO
import org.scalajs.dom.Event

import scala.language.implicitConversions
import scala.scalajs.js

package object frontend {
  type EventHandler = js.Function1[Event, fs2.Stream[IO, Any]]

  implicit class PimpedEventType(eventType: DOMEventType) {
    def → (handler: EventHandler): EventListener = EventListener(eventType, handler)
  }

}
