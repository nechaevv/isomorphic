package com.github.nechaevv.isomorphic.dom

import com.github.nechaevv.isomorphic.EventDispatcher
import com.github.nechaevv.isomorphic.api.WeakMap
import org.w3c.dom.html.HTMLElement

object DomReconciler {

  private val componentCache = new WeakMap[ComponentNode[_], Node]()
  private val nodes = new WeakMap[org.w3c.dom.Node, Node]

  def apply(vdom: Seq[Node], context: DomReconciliationContext): DomReconciliationContext = {
    reconcileNodeSeq(0, context.currentVDom, vdom, context.container, context.eventDispatcher)
    context.copy(currentVDom = vdom)
  }

  private def reconcileNodeSeq(offset: Int, currentVDom: Seq[Node], vdom: Seq[Node], container: HTMLElement, eventDispatcher: EventDispatcher): Unit = {
    var containerOffset = offset

    for (node ← vdom) {

    }
  }

}

case class DomReconciliationContext(container: HTMLElement, eventDispatcher: EventDispatcher, currentVDom: Seq[Node])