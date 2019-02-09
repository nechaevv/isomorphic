package com.github.nechaevv.isomorphic

/*
import api.{React, ReactElement}
import frontend._
import org.scalajs.dom.Event

import scala.scalajs.js
import scala.scalajs.js.Dictionary

class ReactRenderer(eventStream: ActionDispatcher) extends Renderer[ReactElement] {

  override def element(name: String, modifiers: ElementModifier*): ReactElement = {
    val isCustomElement = name.contains("-") || modifiers.exists(m ⇒ m.isInstanceOf[Attribute] && m.asInstanceOf[Attribute].name == "is")
    val props = Dictionary.empty[js.Any]
    var childElements: Seq[ReactElement] = Seq.empty
    var classes: Seq[String] = Seq.empty
    def parseModifiers(mods: Iterable[ElementModifier]): Unit = mods foreach {
      case Attribute(n, v) ⇒ props(mapAttributeName(n, isCustomElement)) = v
      case EventListener(e, h) ⇒ props("on" + e.name) = (e: Event) ⇒ h(e).through(eventStream.enqueue).compile.drain.unsafeRunAsyncAndForget()
      case ChildElement(e) ⇒ childElements = childElements :+ e(this)
      case ChildComponent(c, s) ⇒ childElements = childElements :+ c(s)(this)
      case MultiModifier(mm) ⇒ parseModifiers(mm)
      case WithClass(className) ⇒ classes = classes :+ className
    }
    parseModifiers(modifiers)
    if (classes.nonEmpty) props(mapAttributeName("class", isCustomElement)) = classes.mkString(" ")
    React.createElement(name, props, childElements:_*)
  }

  private def mapAttributeName(name: String, isCustomElement: Boolean): String = {
    if (isCustomElement) name
    else name match {
      case "class" ⇒ "className"
      case s ⇒ s
    }
  }

  override def fragment(contents: ReactElement*): ReactElement = {
    React.createElement(React.Fragment, js.Dictionary(), contents.asInstanceOf[Seq[js.Any]]:_*)
  }

  override def text(content: String): ReactElement = {
    React.createElement(React.Fragment, js.Dictionary(), content)
  }

}
*/
