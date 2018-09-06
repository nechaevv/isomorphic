package com.github.nechaevv.react

import com.github.nechaevv.core.Renderer

import scala.scalajs.js
import js.JSConverters._

object ReactRenderer extends Renderer {
  override type Element = ReactElement
  override type Fragment = ReactElement

  override def element(name: String, attributes: Map[String, String], children: Element*): Element = {
    React.createElement(name, attributes.toJSDictionary, children.asInstanceOf[Seq[js.Any]]:_*)
  }
  override def fragment(contents: Element*): Element = {
    React.createElement(React.Fragment, js.Dictionary(), contents.asInstanceOf[Seq[js.Any]]:_*)
  }
  override def text(content: String): Element = {
    React.createElement(React.Fragment, js.Dictionary(), content)
  }
}
