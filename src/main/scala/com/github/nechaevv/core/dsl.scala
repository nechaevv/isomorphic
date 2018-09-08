package com.github.nechaevv.core

import org.scalajs.dom.Event

import scala.language.implicitConversions

object dsl {
  type EventHandler = Event ⇒ Unit

  trait ElementModifier
  case class Attribute(name: String, value: String) extends ElementModifier
  case class EventListener(eventType: EventType, handler: Event ⇒ Unit) extends ElementModifier
  case class ChildElement(element: Element) extends ElementModifier
  case class MultiModifier(mods: Iterable[ElementModifier]) extends ElementModifier

  class Tag(name: String) {
    type AttributeDef = (String, String)
    type EventListenerDef = (EventType, EventHandler)

    def apply(modifiers: ElementModifier*): Element = {

      def parseModifiers(init: (Seq[AttributeDef], Seq[EventListenerDef], Seq[Element]),
                         modifiers: Iterable[ElementModifier]): (Seq[AttributeDef], Seq[EventListenerDef], Seq[Element]) = {
        modifiers.foldLeft(init) { (acc, mod) ⇒
          val (attrs, els, childs) = acc
          mod match {
            case Attribute(attrName, value) ⇒ (attrs :+ (attrName, value), els, childs)
            case EventListener(eventType, handler) ⇒ (attrs, els :+ (eventType, handler), childs)
            case ChildElement(element) ⇒ (attrs, els, childs :+ element)
            case MultiModifier(mods) ⇒ parseModifiers(acc, mods)
          }
        }
      }

      val (attributes, eventListeners, children) = parseModifiers((Seq.empty[(String, String)], Seq.empty[EventListenerDef], Seq.empty[Element]), modifiers)

      new Element {
        override def apply[E](renderer: Renderer[E]): E = renderer.element(name, attributes, eventListeners, children.map(_.apply(renderer)))
      }
    }
  }

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

  def div = new Tag("div")
  def h1 = new Tag("h1")
  def h2 = new Tag("h2")
  def ul = new Tag("ul")
  def li = new Tag("li")
  def form = new Tag("form")
  def input = new Tag("input")
  def button = new Tag("button")

}
