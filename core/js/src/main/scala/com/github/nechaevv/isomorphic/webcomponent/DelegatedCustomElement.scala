package com.github.nechaevv.isomorphic.webcomponent

import org.scalajs.dom.raw.HTMLElement

abstract class DelegatedCustomElement(delegateFactory: HTMLElement ⇒ CustomElementDelegate) extends HTMLElement {

  private var delegate: CustomElementDelegate = NoopCustomElementDelegate

  def connectedCallback(): Unit = {
    delegate = delegateFactory(this)
    delegate.connectedCallback()
  }

  def disconnectedCallback(): Unit = {
    delegate.disconnectedCallback()
    delegate = NoopCustomElementDelegate
  }

  def adoptedCallback(): Unit = delegate.adoptedCallback()

  def attributeChangedCallback(name: String, oldValue: String, newValue: String): Unit = delegate
    .attributeChangedCallback(name, oldValue, newValue)

}
