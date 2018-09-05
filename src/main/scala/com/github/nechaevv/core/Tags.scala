package com.github.nechaevv.core

class Tags[T](renderer: Renderer[T]) {
  type HtmlTag = (Map[String, String], Seq[T]) ⇒ T

  def htmlTag(name: String)(attributes: Map[String, String], children: T*): T = {
    renderer.element(name, attributes, children)
  }

  val h1: HtmlTag = htmlTag("h1")
  val h2: HtmlTag = htmlTag("h2")

}
