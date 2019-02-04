package com.github.nechaevv.isomorphic.dom

import scala.language.implicitConversions

sealed trait Node {
  def key: Option[String]
}

case class TagNode(name: String, properties: Seq[NodeProperty] = Nil, children: Seq[Node] = Nil, key: Option[String] = None) extends Node {
  def withKey(k: String): TagNode = this.copy(key = Some(k))
  def attr(newProperties: NodeProperty*): TagNode = this.copy(properties = newProperties)
  def apply(newChildren: Node*): TagNode = this.copy(children = newChildren)
}
case class TextNode(text: String) extends Node {
  override def key: Option[String] = None
}
case class ComponentNode[S, +N <: Node](component: Component[S, N], state: S, key: Option[String] = None) extends Node {
  def withKey(k: String): ComponentNode[S, N] = this.copy(key = Some(k))
  def eval(): N = component(state)
}
case class FragmentNode(children: Seq[Node], key: Option[String] = None) extends Node {
  def withKey(k: String): FragmentNode = this.copy(key = Some(k))
}

trait NodeProperty

case class NodeAttribute(name: String, value: String) extends NodeProperty
case class NodeClass(name: String) extends NodeProperty
case object EmptyProperty extends NodeProperty

object dsl {
  implicit class PimpedString(s: String) {
    def :=(value: String): NodeAttribute = NodeAttribute(s, value)
    def :=(value: Boolean): NodeAttribute = NodeAttribute(s, if (value) "true" else "false")
    def ?=(value: Boolean): NodeProperty = if (value) NodeAttribute(s, "") else EmptyProperty
  }
  implicit class PimpedSymbol(s: Symbol) extends PimpedString(s.name)
  implicit class PimpedComponent[S, N <: Node](c: Component[S, N]) {
    def << (state: S): ComponentNode[S, N] = ComponentNode(c, state)
  }
  implicit def textToNode(text: String): Node = TextNode(text)
  def fragment(children: Node*): FragmentNode = FragmentNode(children)
  implicit def seqToNode(seq: Seq[Node]): FragmentNode = FragmentNode(seq)
  implicit def optionToNode(o: Option[Node]): FragmentNode = FragmentNode(o.toSeq)
}