package com.github.nechaevv.isomorphic

import com.github.nechaevv.isomorphic.frontend._
import org.scalajs.dom.{Node, document}

object DomRenderer extends Renderer[Node] {
  override def element(name: String, modifiers: ElementModifier*): Node = {
    val node = document.createElement(name)
    def parseModifiers(mods: ElementModifier*): Unit = mods foreach {
      case Attribute(n, v) ⇒ node.setAttribute(n, v)
      case EventListener(e, h) ⇒ node.addEventListener(e.name.toLowerCase, h, e.isCapturing)
      case ChildElement(e) ⇒ node.appendChild(e(this))
      case MultiModifier(mm) ⇒ parseModifiers(mm)
    }
    parseModifiers(modifiers)
    node
  }

  override def fragment(contents: Node*): Node = {
    val fragment = document.createDocumentFragment()
    for (child ← contents) fragment.appendChild(child)
    fragment
  }

  override def text(content: String): Node = document.createTextNode(content)

}
