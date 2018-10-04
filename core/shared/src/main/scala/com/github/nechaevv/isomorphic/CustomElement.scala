package com.github.nechaevv.isomorphic

trait CustomElement {
  def elementName: String
}

trait AutonomousCustomElement extends CustomElement {

}

trait ExtensionCustomElement extends CustomElement {
  def extendedElement: String
}