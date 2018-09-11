package com.github.nechaevv.sjsui.dom

import com.github.nechaevv.sjsui.WebComponent

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal
class CustomElementRegistry extends js.Object {
  def define(name: String, constructor: js.Dynamic): Unit = js.native
}

object CustomElementRegistry {
  val instance: CustomElementRegistry = js.Dynamic.global.window.customElements.asInstanceOf[CustomElementRegistry]

  def register(components: (String, js.Dynamic)*): Unit = components foreach {
    case (name, constructor) ⇒ instance.define(name, constructor)
  }
}