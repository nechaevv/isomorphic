package com.github.nechaevv.isomorphic

import scala.language.implicitConversions

trait UiPlatform extends Tags {
  type Event
  type Component[S, E] = (S, EventDispatcher[E]) ⇒ Element
  type EventHandler = Event ⇒ Unit

  val functionCache: FunctionCache

  trait Element {
    def apply[E](renderer: Renderer[E]): E
  }

  trait Renderer[E] {
    def element(name: String, attributes: Iterable[(String, String)], eventListeners: Iterable[(EventType, Event ⇒ Unit)], childElements: Seq[E]): E
    def fragment(contents: E*): E
    def text(content: String): E
  }

  trait ElementModifier
  case class Attribute(name: String, value: String) extends ElementModifier
  case class EventListener(eventType: EventType, handler: Event ⇒ Unit) extends ElementModifier
  case class ChildElement(element: Element) extends ElementModifier
  case class MultiModifier(mods: Iterable[ElementModifier]) extends ElementModifier

  implicit class PimpedString(s: String) {
    def :=(value: String) : Attribute = Attribute(s, value)
    def :?(value: Boolean) : MultiModifier = MultiModifier(if (value) Seq(Attribute(s, s)) else Seq.empty[Attribute])
  }

  implicit class PimpedEventType(eventType: EventType) {
    def →(handler: Event ⇒ Unit): EventListener = EventListener(eventType, handler)
  }

  implicit def childElementModifier(element: Element) : ChildElement = ChildElement(element)
  implicit def childTextModifier(text: String): ChildElement = ChildElement(new Element {
    override def apply[E](renderer: Renderer[E]): E = renderer.text(text)
  })
  implicit def modifierIterableImplicit[T](mods: Iterable[T])(implicit conv: T ⇒ ElementModifier): MultiModifier = MultiModifier(mods.map(conv))
  implicit def modifierOptionImplicit[T](mod: Option[T])(implicit conv: T ⇒ ElementModifier): MultiModifier = MultiModifier(mod.map(conv))

}
