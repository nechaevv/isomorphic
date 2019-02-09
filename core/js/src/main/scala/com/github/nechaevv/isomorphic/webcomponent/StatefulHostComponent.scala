package com.github.nechaevv.isomorphic.webcomponent

import com.github.nechaevv.isomorphic._
import com.github.nechaevv.isomorphic.api.{HTMLElementWithShadowRoot, ReactDOM}
import com.github.nechaevv.isomorphic.vdom.{ComponentVNode, DomReconciler, FragmentVNode}
import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js

trait StatefulHostComponent {
  type State <: AnyRef

  def attributes: Iterable[String] = Seq.empty
  def initialState(properties: Iterable[(String, String)]): State
  def reducer: Any ⇒ State ⇒ State = _ ⇒ s ⇒ s
  def effect: Any ⇒ State ⇒ ActionStream = _ ⇒ _ ⇒ fs2.Stream.empty
  def render(componentHost: HTMLElement): ActionDispatcher ⇒ State ⇒ Unit
  def customElementDelegate(componentHost: HTMLElement): StatefulHostElementDelegate = {
    new StatefulHostElementDelegate(this, componentHost)
  }

}

case object ComponentConnectedEvent
case object ComponentDisconnectedEvent
case object ComponentAdoptedEvent
case class ComponentAttributeChangedEvent(name: String, oldValue: String, newValue: String)
/*
trait ReactRender { this: StatefulHostComponent ⇒
  def rootComponent: State ⇒ Element

  override def render(componentHost: HTMLElement): ActionDispatcher ⇒ State ⇒ Unit = {
    actionDispatcher ⇒ state ⇒ {
      val vdom = rootComponent(state)(new ReactRenderer(actionDispatcher))
      ReactDOM.render(vdom, componentHost)
    }
  }
}
*/

trait DomReconcilerRender { this: StatefulHostComponent ⇒
  def rootComponent: vdom.Component[State, FragmentVNode]

  override def render(componentHost: HTMLElement): ActionDispatcher ⇒ State ⇒ Unit = {
    actionDispatcher ⇒ state ⇒ {
      val vdom = ComponentVNode(rootComponent, state)
      DomReconciler.reconcileRootComponent(componentHost, vdom, actionDispatcher)
    }
  }
}

trait ShadowDomReconcilerRender { this: StatefulHostComponent ⇒
  def rootComponent: vdom.Component[State, FragmentVNode]
  def isOpen: Boolean = false
  override def render(componentHost: HTMLElement): ActionDispatcher ⇒ State ⇒ Unit = {
    val shadowRoot = componentHost.asInstanceOf[HTMLElementWithShadowRoot]
      .attachShadow(js.Dynamic.literal("mode" → (if (isOpen) "open" else "closed")))
    actionDispatcher ⇒ state ⇒ {
      val vdom = ComponentVNode(rootComponent, state)
      DomReconciler.reconcileRootComponent(shadowRoot, vdom, actionDispatcher)
    }
  }
}
