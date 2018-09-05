package com.github.nechaevv.react

import com.github.nechaevv.core.Renderer

import scala.scalajs.js
import js.JSConverters._

object ReactRenderer extends Renderer[Element] {

  override def element(name: String, attributes: Map[String, String], children: Seq[Element]): Element = {
    React.createElement(name, attributes, children.asInstanceOf[Seq[js.Any]]:_*)
  }
  override def fragment(contents: Element*): Element = {
    React.createElement(React.Fragment, Nil, contents.asInstanceOf[Seq[js.Any]]:_*)
  }
  override def text(content: String): Element = {
    React.createElement(React.Fragment, Nil, content)
  }
}
