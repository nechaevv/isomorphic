package com.github.nechaevv.isomorphic

import org.scalajs.dom.Event

package object frontend {
  type EventHandler = Event ⇒ Unit

  implicit class PimpedEventType(eventType: DOMEventType) {
    def →(handler: Event ⇒ Unit): EventListener = EventListener(eventType, handler)
  }

}
