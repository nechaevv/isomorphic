package com.github.nechaevv.isomorphic.react

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSGlobal, JSImport}

@js.native
@JSImport("react", JSImport.Default)
object React extends js.Object {
  def createElement(element: js.Any, attributes: js.Dictionary[js.Any], children: js.Any*): ReactElement = js.native
  val Fragment: js.Any = js.native
}
