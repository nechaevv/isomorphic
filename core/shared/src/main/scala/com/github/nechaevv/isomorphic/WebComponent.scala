package com.github.nechaevv.isomorphic

trait WebComponent {
  def elementName: String
}

trait AutonomousWebComponent extends WebComponent {

}

trait ExtensionWebComponent extends WebComponent {
  def extendedElement: String
}