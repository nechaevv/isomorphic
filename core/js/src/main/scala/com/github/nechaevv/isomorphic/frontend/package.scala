package com.github.nechaevv.isomorphic

import org.scalajs.dom.Event

package object frontend {
  type EventHandler = Event ⇒ Iterable[Any]

  implicit class PimpedEventType(eventType: DOMEventType) {
    def ->>(handler: Event ⇒ Iterable[Any]): EventListener = EventListener(eventType, handler)
    def →(handler: Event ⇒ Any): EventListener = EventListener(eventType, Some(_))
    def →(action: Any): EventListener = EventListener(eventType, _ ⇒ Some(action))
  }

}
