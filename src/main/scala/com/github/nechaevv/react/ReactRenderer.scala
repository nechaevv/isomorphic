package com.github.nechaevv.react

import com.github.nechaevv.core.Renderer

import scala.scalajs.js
import js.JSConverters._

object ReactRenderer extends Renderer[ReactElement] {

  override def element(name: String, attributes: Map[String, String], children: ReactElement*): ReactElement = {
    React.createElement(name, attributes.toJSDictionary, children.asInstanceOf[Seq[js.Any]]:_*)
  }

  override def fragment(contents: ReactElement*): ReactElement = {
    React.createElement(React.Fragment, js.Dictionary(), contents.asInstanceOf[Seq[js.Any]]:_*)
  }

  override def text(content: String): ReactElement = {
    React.createElement(React.Fragment, js.Dictionary(), content)
  }

}
