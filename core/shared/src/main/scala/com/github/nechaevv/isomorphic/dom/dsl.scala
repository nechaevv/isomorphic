package com.github.nechaevv.isomorphic.dom

import scala.language.implicitConversions

sealed trait Node

case class Tag(name: String, properties: Seq[NodeProperty] = Nil, children: Seq[Node] = Nil) extends Node {
  def apply(newProperties: NodeProperty*): Tag = this.copy(properties = newProperties)
  def apply(newChildren: Node*): Tag = this.copy(children = newChildren)
}
case class Text(text: String) extends Node
case class ComponentNode[S](component: Component[S], state: S) extends Node
case class Fragment(children: Node*) extends Node

trait NodeProperty

case class NodeAttribute(name: String, value: String) extends NodeProperty
case class NodeClass(name: String) extends NodeProperty
case class NodeId(id: AnyRef) extends NodeProperty

object dsl {
  implicit class PimpedString(s: String) {
    def :=(value: String): NodeAttribute = NodeAttribute(s, value)
  }
  implicit class PimpedSymbol(s: Symbol) extends PimpedString(s.name)
  implicit class PimpedComponent[S](c: Component[S]) {
    def << (state: S): ComponentNode[S] = ComponentNode(c, state)
  }
  implicit def textToNode(text: String): Node = Text(text)
}