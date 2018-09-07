package com.github.nechaevv.core

import org.scalajs.dom.Event

trait Renderer[Element] {
  def element(name: String, attributes: Iterable[(String, String)], eventListeners: Iterable[(EventType, Event ⇒ Unit)], childElements: Seq[Element]): Element
  def fragment(contents: Element*): Element
  def text(content: String): Element
}
