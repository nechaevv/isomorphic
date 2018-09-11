package com.github.nechaevv.sjsui.example

import com.github.nechaevv.sjsui.dom.CustomElementRegistry

object Main {
  def main(args: Array[String]): Unit = {
    CustomElementRegistry.register(TasksApp)
  }
}