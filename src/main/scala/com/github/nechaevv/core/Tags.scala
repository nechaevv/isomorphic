package com.github.nechaevv.core

object Tags {
  type Tag[E] = Renderer[E] ⇒ (Map[String, String], Seq[E]) ⇒ E

  def tag[E](name: String): Tag[E] = { renderer: Renderer[E] ⇒ { (attributes: Map[String, String], children: Seq[E]) ⇒
    renderer.element(name, attributes, children:_*)
  } }

  def div[E]: Tag[E] = tag("div")
  def h1[E]: Tag[E] = tag("h1")
  def h2[E]: Tag[E] = tag("h2")
  def ul[E]: Tag[E] = tag("ul")
  def li[E]: Tag[E] = tag("li")
  def form[E]: Tag[E] = tag("form")
  def input[E]: Tag[E] = tag("input")

}
