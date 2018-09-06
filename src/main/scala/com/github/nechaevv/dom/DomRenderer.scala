package com.github.nechaevv.dom

import com.github.nechaevv.core.Renderer
import org.scalajs.dom.raw.Node
import org.scalajs.dom.{Event, document}

object DomRenderer extends Renderer[Node] {

  override def element(name: String, attributes: Map[String, String], events: Map[String, Event ⇒ Unit], children: Node*): Node = {
    val node = document.createElement(name)
    for ((n, v) ← attributes) node.setAttribute(n, v)
    for (child ← children) node.appendChild(child)
    node
  }

  override def fragment(contents: Node*): Node = {
    val fragment = document.createDocumentFragment()
    for (child ← contents) fragment.appendChild(child)
    fragment
  }

  override def text(content: String): Node = document.createTextNode(content)

}
