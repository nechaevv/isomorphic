package com.github.nechaevv.isomorphic.dom

import com.github.nechaevv.isomorphic.EventDispatcher
import com.github.nechaevv.isomorphic.api.WeakMap
import org.w3c.dom.html.HTMLElement

import scala.scalajs.js

object DomReconciler {

  private val componentCache = new WeakMap[ComponentNode[_], Node]()
  private val nodeReprCache = new WeakMap[Node, NodeRepr]
  private val childNodeCache = new WeakMap[NodeRepr, Map[String, NodeRepr]]

  trait NodeRepr {
    def key: String
  }

  case class TagRepr(name: String, attrs: Map[String, String], eventListeners: Set[NodeEventListener],
                     children: Seq[NodeRepr], key: String) extends NodeRepr
  case class FragmentRepr(children: Seq[NodeRepr], key: String) extends NodeRepr
  case class TextRepr(text:String, key: String) extends NodeRepr

  def apply(vdom: Seq[Node], context: DomReconciliationContext): DomReconciliationContext = {
    reconcileNodeSeq(context.currentVDom, vdom, context.container, context.eventDispatcher)
    context.copy(currentVDom = vdom)
  }

  private def reconcileNodeSeq(currentVDom: Seq[Node], vdom: Seq[Node], container: HTMLElement,
                               eventDispatcher: EventDispatcher): Unit = {
    var index = 0
    for (node ← vdom) {
      var key = node.key.getOrElse({ index += 1; index.toString })

    }
  }

  /*
  private def buildRepr(vdom: Seq[Node]): Seq[NodeRepr] = vdom map {
    case TagNode(name, props, children, key) ⇒ TagRepr(name,
      props.collect({
        case NodeAttribute(attrName, value) ⇒ attrName → value
      }).toMap,
      props.collect({
        case e: NodeEventListener ⇒ e
      }).toSet,
      buildRepr(children)
    )
    case TextNode(s) ⇒ TextRepr(s)
    case f: FragmentNode ⇒ FragmentRepr(buildRepr(f.children))
    case cn: ComponentNode[_] ⇒
      val cached
  }
  */

}

case class DomReconciliationContext(container: HTMLElement, eventDispatcher: EventDispatcher, currentVDom: Seq[Node])