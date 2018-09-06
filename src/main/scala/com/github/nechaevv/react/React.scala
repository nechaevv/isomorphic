package com.github.nechaevv.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal
object React extends js.Object {
  def createElement(element: js.Any, attributes: js.Dictionary[String], children: js.Any*): ReactElement = js.native
  val Fragment: js.Any = js.native
}
