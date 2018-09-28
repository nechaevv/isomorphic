package com.github.nechaevv.isomorphic

trait ElementModifier

case class Attribute(name: String, value: String) extends ElementModifier
case class ChildElement(element: Element) extends ElementModifier
case class MultiModifier(mods: Iterable[ElementModifier]) extends ElementModifier
