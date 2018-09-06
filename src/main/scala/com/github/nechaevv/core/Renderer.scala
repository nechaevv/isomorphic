package com.github.nechaevv.core

import org.scalajs.dom.Event

trait Renderer[Element] {
  def element(name: String, attributes: Map[String, String], events: Map[String, Event⇒Unit], children: Element*): Element
  def fragment(contents: Element*): Element
  def text(content: String): Element
}
