package com.github.nechaevv.isomorphic.api

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("react", JSImport.Default)
object React extends js.Object {
  def createElement(element: js.Any, attributes: js.Dictionary[js.Any], children: js.Any*): ReactElement = js.native
  val Fragment: js.Any = js.native
}
