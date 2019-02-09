package com.github.nechaevv.isomorphic

/*

import com.github.nechaevv.isomorphic.browser._
import org.scalajs.dom.{Event, Node, document}

class DomRenderer(eventStream: ActionDispatcher)  extends Renderer[Node] {
  override def element(name: String, modifiers: ElementModifier*): Node = {
    val node = document.createElement(name)
    var classes: Seq[String] = Seq.empty
    def parseModifiers(mods: Seq[ElementModifier]): Unit = mods foreach {
      case Attribute(n, v) ⇒ node.setAttribute(n, v)
      case EventListener(e, h) ⇒ node.addEventListener(e.name.toLowerCase,
        (e: Event) ⇒ h(e).through(eventStream.enqueue).compile.drain.unsafeRunAsyncAndForget(), e.isCapturing)
      case ChildElement(e) ⇒ node.appendChild(e(this))
      case ChildComponent(c, s) ⇒ node.appendChild(c(s)(this))
      case WithClass(className) ⇒ classes = classes :+ className
      case MultiModifier(mm) ⇒ parseModifiers(mm)
    }
    parseModifiers(modifiers)
    if (classes.nonEmpty) node.setAttribute("class", classes.mkString(" "))
    node
  }

  override def fragment(contents: Node*): Node = {
    val fragment = document.createDocumentFragment()
    for (child ← contents) fragment.appendChild(child)
    fragment
  }

  override def text(content: String): Node = document.createTextNode(content)

}

*/
