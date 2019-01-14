package com.github.nechaevv.isomorphic

class Tag(name: String) {

  def apply(modifiers: ElementModifier*): Element = {
    new Element {
      override def apply[E](renderer: Renderer[E]): E = renderer.element(name, modifiers: _*)
    }
  }

  def append(newModifiers: ElementModifier*): Tag = new Tag(name) {
    override def apply(modifiers: ElementModifier*): Element = super.apply(modifiers ++ newModifiers: _*)
  }

  def prepend(newModifiers: ElementModifier*): Tag = new Tag(name) {
    override def apply(modifiers: ElementModifier*): Element = super.apply(newModifiers ++ modifiers: _*)
  }

}

object Tags {

  def fragment(elements: Element*): Element = new Element {
    override def apply[E](renderer: Renderer[E]): E = renderer.fragment(elements.map(element ⇒ element(renderer)): _*)
  }

  val a      = new Tag("a")
  val button = new Tag("button")
  val div    = new Tag("div")
  val footer = new Tag("footer")
  val form   = new Tag("form")
  val h1     = new Tag("h1")
  val h2     = new Tag("h2")
  val header = new Tag("header")
  val img    = new Tag("img")
  val input  = new Tag("input")
  val label  = new Tag("label")
  val li     = new Tag("li")
  val option = new Tag("option")
  val p      = new Tag("p")
  val section= new Tag("section")
  val select = new Tag("select")
  val slot   = new Tag("slot")
  val span   = new Tag("span")
  val table  = new Tag("table")
  val tbody  = new Tag("tbody")
  val td     = new Tag("td")
  val th     = new Tag("th")
  val thead  = new Tag("thead")
  val tr     = new Tag("tr")
  val ul     = new Tag("ul")

}
