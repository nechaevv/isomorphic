package com.github.nechaevv.core

trait Renderer {
  type Element
  type Fragment

  def element(name: String, attributes: Map[String, String], children: Element*): Element
  def fragment(contents: Element*): Fragment
  def text(content: String): Element
}
