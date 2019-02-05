package com.github.nechaevv.isomorphic.vdom

import scala.language.implicitConversions

sealed trait VNode {
  def key: Option[String]
}

case class ElementVNode(name: String, modifiers: Seq[VNodeModifier] = Nil, key: Option[String] = None) extends VNode {
  def withKey(k: String): ElementVNode = this.copy(key = Some(k))
  def apply(newModifiers: VNodeModifier*): ElementVNode = this.copy(modifiers = modifiers ++ newModifiers)
}
case class TextVNode(text: String) extends VNode {
  override def key: Option[String] = None
}
case class ComponentVNode[S, +N <: VNode](component: Component[S, N], state: S, key: Option[String] = None) extends VNode {
  def withKey(k: String): ComponentVNode[S, N] = this.copy(key = Some(k))
  def eval(): N = component(state)
}
case class FragmentVNode(children: Seq[VNode], key: Option[String] = None) extends VNode {
  def withKey(k: String): FragmentVNode = this.copy(key = Some(k))
}

trait VNodeModifier

case class VNodeAttribute(name: String, value: String) extends VNodeModifier
case class VNodeClass(name: String) extends VNodeModifier
case class VNodeChild(node: VNode) extends VNodeModifier
case object EmptyModifier extends VNodeModifier
