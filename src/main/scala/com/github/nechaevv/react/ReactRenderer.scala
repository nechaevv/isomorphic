package com.github.nechaevv.react

import com.github.nechaevv.core.Renderer
import org.scalajs.dom.Event

import scala.scalajs.js
import js.JSConverters._
import scala.scalajs.js.Dictionary

object ReactRenderer extends Renderer[ReactElement] {

  override def element(name: String, attributes: Map[String, String], events: Map[String, Event ⇒ Unit], children: ReactElement*): ReactElement = {
    val props = Dictionary.empty[js.Any]
    attributes.foreach {
      case (k, v) ⇒ props(k) = v
    }
    events.foreach {
      case (k, v ) ⇒ props(k) = v
    }
    React.createElement(name, props, children.asInstanceOf[Seq[js.Any]]:_*)
  }

  override def fragment(contents: ReactElement*): ReactElement = {
    React.createElement(React.Fragment, js.Dictionary(), contents.asInstanceOf[Seq[js.Any]]:_*)
  }

  override def text(content: String): ReactElement = {
    React.createElement(React.Fragment, js.Dictionary(), content)
  }

}
