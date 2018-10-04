package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic.CustomElements

import scala.scalajs.js

object Main {
  def main(args: Array[String]): Unit = {
    CustomElements.define(TasksApp, js.constructorOf[TasksAppStatefulHostCustomElement])
  }
}