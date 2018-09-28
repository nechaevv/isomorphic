package com.github.nechaevv.isomorphic

import com.github.nechaevv.isomorphic.api.CustomElementRegistry

import scala.scalajs.js
import scala.scalajs.js.Dynamic


object WebComponents {
  val registry: CustomElementRegistry = js.Dynamic.global.window.customElements.asInstanceOf[CustomElementRegistry]

  def define[T <: WebComponent](webComponent: T)(implicit customElementDef: CustomElementDef[T]): Unit = webComponent match {
    case component: ExtensionWebComponent ⇒ registry.define(component.elementName, customElementDef.constructor, Dynamic.literal("extends" → component.extendedElement))
    case component ⇒ registry.define(component.elementName, customElementDef.constructor)
  }

}

class CustomElementDef[T <: WebComponent](val constructor: js.Dynamic)