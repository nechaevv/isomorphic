package com.github.nechaevv.isomorphic.dom

import com.github.nechaevv.isomorphic.EventDispatcher
import com.github.nechaevv.isomorphic.api.WeakMap
import org.w3c.dom.html.HTMLElement

import scala.scalajs.js

object DomReconciler {

  private val componentCache = new WeakMap[ComponentNode[_], Node]()
  private val childComponentDictionary = new WeakMap[Node, js.Dictionary[Node]]

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

}

case class DomReconciliationContext(container: HTMLElement, eventDispatcher: EventDispatcher, currentVDom: Seq[Node])