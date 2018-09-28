package com.github.nechaevv.isomorphic

class Tag(name: String) {

  def apply(modifiers: ElementModifier*): Element = {
    new Element {
      override def apply[E](renderer: Renderer[E]): E = renderer.element(name, modifiers)
    }
  }

  def append(newModifiers: ElementModifier*): Tag = new Tag(name) {
    override def apply(modifiers: ElementModifier*): Element = super.apply(modifiers ++ newModifiers)
  }

  def prepend(newModifiers: ElementModifier*): Tag = new Tag(name) {
    override def apply(modifiers: ElementModifier*): Element = super.apply(newModifiers ++ modifiers)
  }

}

object Tags {

  def fragment(elements: Element*): Element = new Element {
    override def apply[E](renderer: Renderer[E]): E = renderer.fragment(elements.map(element ⇒ element(renderer)):_*)
  }

  def a = new Tag("a")
  def button = new Tag("button")
  def div = new Tag("div")
  def footer = new Tag("footer")
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
