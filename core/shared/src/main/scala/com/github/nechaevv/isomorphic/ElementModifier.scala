package com.github.nechaevv.isomorphic

trait ElementModifier

case class Attribute(name: String, value: String) extends ElementModifier
case class ChildElement(element: Element) extends ElementModifier
case class ChildComponent[S](component: S ⇒ Element, state: S) extends ElementModifier
case class MultiModifier(mods: Seq[ElementModifier]) extends ElementModifier
case class WithClass(className: String) extends ElementModifier