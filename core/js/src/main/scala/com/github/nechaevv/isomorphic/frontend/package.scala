package com.github.nechaevv.isomorphic

import cats.effect.IO
import org.scalajs.dom.Event

import scala.language.implicitConversions

package object frontend {
  type EventHandler = Event ⇒ fs2.Stream[IO, Any]

  implicit class PimpedEventType(eventType: DOMEventType) {
    def → (handler: EventHandler): EventListener = EventListener(eventType, handler)
  }

}
