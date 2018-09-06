package com.github.nechaevv.react

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal
object ReactDOM extends js.Object {
  def render(element: ReactElement, container: dom.Element): Unit = js.native
}
