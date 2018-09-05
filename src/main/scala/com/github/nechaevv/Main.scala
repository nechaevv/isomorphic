package com.github.nechaevv

import com.github.nechaevv.react.ReactDOM
import org.scalajs.dom
import dom.document
import react.Tags._
import com.github.nechaevv.react.ReactRenderer._

object Main {
  def main(args: Array[String]): Unit = {
    val content = h1(Map.empty, Seq(
      text("Hello, world!")
    ))
    val container = document.getElementById("Container")

    ReactDOM.render(content, container)

  }
}
