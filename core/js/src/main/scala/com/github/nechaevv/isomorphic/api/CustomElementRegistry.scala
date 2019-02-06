package com.github.nechaevv.isomorphic.api

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal
class CustomElementRegistry extends js.Object {
  def define(name: String, constructor: js.Dynamic): Unit = js.native
  def define(name: String, constructor: js.Dynamic, options: js.Dynamic): Unit = js.native
}

