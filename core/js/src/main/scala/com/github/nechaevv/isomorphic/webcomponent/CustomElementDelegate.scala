package com.github.nechaevv.isomorphic.webcomponent

class CustomElementDelegate {
  def connectedCallback(): Unit = ()
  def disconnectedCallback(): Unit = ()
  def adoptedCallback(): Unit = ()
  def attributeChangedCallback(name: String, oldValue: String, newValue: String): Unit = ()
}

object NoopCustomElementDelegate extends CustomElementDelegate
