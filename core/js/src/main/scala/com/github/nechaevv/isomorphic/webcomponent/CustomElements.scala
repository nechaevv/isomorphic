package com.github.nechaevv.isomorphic.webcomponent

import com.github.nechaevv.isomorphic.api.CustomElementRegistry
import com.github.nechaevv.isomorphic.{AutonomousCustomElement, CustomElement, ExtensionCustomElement}

import scala.scalajs.js
import scala.scalajs.js.Dynamic

object CustomElements {
  val registry: CustomElementRegistry = js.Dynamic.global.window.customElements.asInstanceOf[CustomElementRegistry]

  def define(customElement: CustomElement, elementConstructor: js.Dynamic): Unit = customElement match {
    case ce: ExtensionCustomElement ⇒ registry.define(ce.elementName, elementConstructor, Dynamic.literal("extends" → ce.extendedElement))
    case ce: AutonomousCustomElement ⇒ registry.define(ce.elementName, elementConstructor)
  }

}
