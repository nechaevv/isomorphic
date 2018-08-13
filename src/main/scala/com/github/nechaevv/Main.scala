package com.github.nechaevv

import org.scalajs.dom
import dom.document

object Main {
  def main(args: Array[String]): Unit = {
    document.getElementById("Container").appendChild(
      h1("Hello, world!").render
    )
  }
}
