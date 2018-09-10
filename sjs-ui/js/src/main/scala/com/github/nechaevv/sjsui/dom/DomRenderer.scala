package com.github.nechaevv.sjsui.dom

import com.github.nechaevv.sjsui.EventType
import com.github.nechaevv.sjsui.platform._
import org.scalajs.dom.{Event, Node, document}

object DomRenderer extends Renderer[Node] {
  override def element(name: String, attributes: Iterable[(String, String)], eventListeners: Iterable[(EventType, Event ⇒ Unit)], childElements: Seq[Node]): Node = {
    val node = document.createElement(name)
    for ((n, v) ← attributes) node.setAttribute(n, v)
    for ((e, h) ← eventListeners) node.addEventListener(e.name.toLowerCase, h, e.isCapturing)
    for (e ← childElements) node.appendChild(e)
    node
  }

  override def fragment(contents: Node*): Node = {
    val fragment = document.createDocumentFragment()
    for (child ← contents) fragment.appendChild(child)
    fragment
  }

  override def text(content: String): Node = document.createTextNode(content)

}
