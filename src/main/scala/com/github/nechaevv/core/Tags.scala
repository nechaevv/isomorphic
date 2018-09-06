package com.github.nechaevv.core

import org.scalajs.dom.raw.Event

object Tags {

  class Tag(name: String) {
    def apply[E](attributes: Map[String, String] = Map.empty, events: Map[String, Event ⇒ Unit] = Map.empty)(children: E*)(implicit renderer: Renderer[E]): E = {
      renderer.element(name, attributes, events, children:_*)
    }
  }

  def div = new Tag("div")
  def h1 = new Tag("h1")
  def h2 = new Tag("h2")
  def ul = new Tag("ul")
  def li = new Tag("li")
  def form = new Tag("form")
  def input = new Tag("input")
  def button = new Tag("button")

}
