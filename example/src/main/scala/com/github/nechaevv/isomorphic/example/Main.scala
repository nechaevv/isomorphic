package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic.dom.CustomElementRegistry

object Main {
  def main(args: Array[String]): Unit = {
    CustomElementRegistry.register(TasksApp)
  }
}