package com.github.nechaevv.isomorphic.api

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@js.native
@JSImport("react-dom", JSImport.Default)
object ReactDOM extends js.Object {
  def render(element: ReactElement, container: dom.Node): Unit = js.native
}
