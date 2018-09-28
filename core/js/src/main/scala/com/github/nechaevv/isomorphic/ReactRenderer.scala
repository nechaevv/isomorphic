package com.github.nechaevv.isomorphic

import api.{React, ReactElement}
import frontend._

import scala.scalajs.js
import scala.scalajs.js.Dictionary

object ReactRenderer extends Renderer[ReactElement] {

  override def element(name: String, modifiers: ElementModifier*): ReactElement = {
    val props = Dictionary.empty[js.Any]
    var childElements: Seq[ReactElement] = Seq.empty
    def parseModifiers(mods: Iterable[ElementModifier]): Unit = mods foreach {
      case Attribute(n, v) ⇒ props(mapAttributeName(n)) = v
      case EventListener(e, h) ⇒ props("on" + e.name) = h
      case ChildElement(e) ⇒ childElements = childElements :+ e(this)
      case MultiModifier(mm) ⇒ parseModifiers(mm)
    }
    parseModifiers(modifiers)
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
