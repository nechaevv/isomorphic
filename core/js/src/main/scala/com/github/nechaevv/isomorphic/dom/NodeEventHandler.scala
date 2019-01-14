package com.github.nechaevv.isomorphic.dom

import cats.effect.IO
import com.github.nechaevv.isomorphic.frontend.DOMEventType
import org.scalajs.dom.Event

case class NodeEventHandler(eventType: DOMEventType, handler: Event ⇒ fs2.Stream[IO, Any]) extends NodeProperty