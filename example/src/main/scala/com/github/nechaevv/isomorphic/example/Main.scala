package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic.webcomponent.CustomElements

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

object Main {
  @JSExportTopLevel("boot")
  def boot(): Unit = {
    CustomElements.define(TasksApp, js.constructorOf[TasksAppStatefulHostCustomElement])
  }
}