package com.github.nechaevv.isomorphic.frontend

import com.github.nechaevv.isomorphic.ElementModifier

case class EventListener(eventType: DOMEventType, handler: EventHandler) extends ElementModifier
