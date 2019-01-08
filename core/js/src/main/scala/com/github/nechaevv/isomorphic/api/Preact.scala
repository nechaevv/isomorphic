package com.github.nechaevv.isomorphic.api

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("preact", JSImport.Default)
object Preact extends js.Object {
  def h(element: js.Any, attributes: js.Dictionary[js.Any], children: js.Any*): ReactElement = js.native
}
