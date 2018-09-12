package com.github.nechaevv.sjsui.react

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSGlobal, JSImport}

@js.native
@JSImport("react-dom", JSImport.Default)
object ReactDOM extends js.Object {
  def render(element: ReactElement, container: dom.Node): Unit = js.native
}
