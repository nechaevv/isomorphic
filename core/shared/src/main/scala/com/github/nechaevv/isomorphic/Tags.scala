package com.github.nechaevv.isomorphic

trait Tags { this: UiPlatform ⇒
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

  def fragment(elements: Element*): Element = new Element {
    override def apply[E](renderer: Renderer[E]): E = renderer.fragment(elements.map(element ⇒ element(renderer)):_*)
  }

  def a = new Tag("a")
  def button = new Tag("button")
  def div = new Tag("div")
  def form = new Tag("form")
  def h1 = new Tag("h1")
  def h2 = new Tag("h2")
  def header = new Tag("header")
  def input = new Tag("input")
  def label = new Tag("label")
  def li = new Tag("li")
  def option = new Tag("option")
  def p = new Tag("p")
  def section = new Tag("section")
  def select = new Tag("select")
  def span = new Tag("span")
  def ul = new Tag("ul")

}
