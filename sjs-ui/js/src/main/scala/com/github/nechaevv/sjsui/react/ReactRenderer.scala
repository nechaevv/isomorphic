package com.github.nechaevv.sjsui.react

import com.github.nechaevv.sjsui.EventType
import com.github.nechaevv.sjsui.platform._

import scala.scalajs.js
import js.JSConverters._
import scala.scalajs.js.Dictionary

object ReactRenderer extends Renderer[ReactElement] {

  override def element(name: String, attributes: Iterable[(String, String)], eventListeners: Iterable[(EventType, Event ⇒ Unit)], childElements: Seq[ReactElement]): ReactElement = {
    val props = Dictionary.empty[js.Any]
    for ((n, v) ← attributes) props(n) = v
    for ((e, h) ← eventListeners) props("on" + e.name) = h
    React.createElement(name, props, childElements:_*)
  }

  override def fragment(contents: ReactElement*): ReactElement = {
    React.createElement(React.Fragment, js.Dictionary(), contents.asInstanceOf[Seq[js.Any]]:_*)
  }

  override def text(content: String): ReactElement = {
    React.createElement(React.Fragment, js.Dictionary(), content)
  }

}
