package com.github.nechaevv.isomorphic.vdom

import cats.effect.IO
import com.github.nechaevv.isomorphic.frontend.DOMEventType
import org.scalajs.dom.Event

case class VNodeEventListener(eventType: DOMEventType, handler: Event ⇒ fs2.Stream[IO, Any]) extends VNodeModifier