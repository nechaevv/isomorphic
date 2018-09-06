package com.github.nechaevv

import com.github.nechaevv.react.{ReactDOM, ReactRenderer}
import org.scalajs.dom.document
import com.github.nechaevv.react.ReactRenderer._
import com.github.nechaevv.core.Tags._

object Main {
  def main(args: Array[String]): Unit = {
    val component = h1(Map("class"→"hello-world"), Seq(
      text("Hello, world!")
    ))
    val container = document.getElementById("Container")

    ReactDOM.render(component(ReactRenderer), container)

  }
}
