package com.github.nechaevv.isomorphic.vdom

import com.github.nechaevv.isomorphic.Component

sealed trait VNode

case class VElement(name: String, attributes: Map[String, String], children: Seq[VNode]) extends VNode
case class VText(text: String) extends VNode
case class VComponent[S](component: Component[S]) extends VNode
