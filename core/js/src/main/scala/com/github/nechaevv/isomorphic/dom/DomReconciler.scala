package com.github.nechaevv.isomorphic.dom

import com.github.nechaevv.isomorphic.EventDispatcher
import com.github.nechaevv.isomorphic.api.WeakMap
import org.w3c.dom.html.HTMLElement

object DomReconciler {

  private val componentCache = new WeakMap[ComponentNode[_], Node]()
  private val nodeReprCache = new WeakMap[Node, NodeRepr]

  trait NodeRepr

  case class TagRepr(name: String, attrs: Map[String, String], eventListeners: Set[NodeEventListener], children: Seq[NodeRepr]) extends NodeRepr
  case class FragmentRepr(children: Seq[NodeRepr]) extends NodeRepr
  case class TextRepr(text:String) extends NodeRepr

  def apply(vdom: Seq[Node], context: DomReconciliationContext): DomReconciliationContext = {
    reconcileNodeSeq(context.currentVDom, vdom, context.container, context.eventDispatcher)
    context.copy(currentVDom = vdom)
  }

  private def buildRepr(vdom: Seq[Node]): Seq[NodeRepr] = vdom map {
    case Tag(name, props, children) ⇒ TagRepr(name,
      props.collect({
        case NodeAttribute(attrName, value) ⇒ attrName → value
      }).toMap,
      props.collect({
        case e: NodeEventListener ⇒ e
      }).toSet,
      buildRepr(children)
    )
    case Text(s) ⇒ TextRepr(s)
    case f: Fragment ⇒ FragmentRepr(buildRepr(f.children))
    case cn: ComponentNode[_] ⇒
       val cached
  }

  private def reconcileNodeSeq(currentVDom: Seq[Node], vdom: Seq[Node], container: HTMLElement,
                               eventDispatcher: EventDispatcher): Unit = {
    for (node ← vdom) {

    }
  }

  def reconcileProps(element: HTMLElement, currentProps: Set[NodeProperty], newProps: Set[NodeProperty]): Unit = {
    for (prop ← newProps) if (!(currentProps contains prop)) prop match {
      case
    }
  }

}

case class DomReconciliationContext(container: HTMLElement, eventDispatcher: EventDispatcher, currentVDom: Seq[Node])