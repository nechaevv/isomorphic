package com.github.nechaevv.core

trait Renderer[Element] {
  def element(name: String, attributes: Map[String, String], children: Element*): Element
  def fragment(contents: Element*): Element
  def text(content: String): Element
}
