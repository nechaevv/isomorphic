package com.github.nechaevv.isomorphic.dom

import com.github.nechaevv.isomorphic.EventDispatcher
import org.w3c.dom.html.HTMLElement

object DomReconciler {
  def apply(vdom: Seq[Node], context: DomReconciliationContext): DomReconciliationContext = {
    reconcileNodeSeq(0, context.currentVDom, vdom, context.container, context.eventDispatcher)
    context.copy(currentVDom = vdom)
  }

  private def reconcileNodeSeq(offset: Int, currentVDom: Seq[Node], vdom: Seq[Node], container: HTMLElement, eventDispatcher: EventDispatcher): Unit = {

  }
}

case class DomReconciliationContext(container: HTMLElement, eventDispatcher: EventDispatcher, currentVDom: Seq[Node])