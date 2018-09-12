package com.github.nechaevv.isomorphic.react

import com.github.nechaevv.isomorphic.EventType
import com.github.nechaevv.isomorphic.platform._

import scala.scalajs.js
import js.JSConverters._
import scala.scalajs.js.Dictionary

object ReactRenderer extends Renderer[ReactElement] {

  override def element(name: String, attributes: Iterable[(String, String)], eventListeners: Iterable[(EventType, Event ⇒ Unit)], childElements: Seq[ReactElement]): ReactElement = {
    val props = Dictionary.empty[js.Any]
    for ((n, v) ← attributes) props(mapAttributeName(n)) = v
    for ((e, h) ← eventListeners) props("on" + e.name) = h
    React.createElement(name, props, childElements:_*)
  }

  def mapAttributeName(name: String): String = name match {
    case "class" ⇒ "className"
    case s ⇒ s
  }

  override def fragment(contents: ReactElement*): ReactElement = {
    React.createElement(React.Fragment, js.Dictionary(), contents.asInstanceOf[Seq[js.Any]]:_*)
  }

  override def text(content: String): ReactElement = {
    React.createElement(React.Fragment, js.Dictionary(), content)
  }

}
