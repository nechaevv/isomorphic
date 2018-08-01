package io.icostack.ui

import org.scalajs.dom
import dom.document
import scalatags.JsDom.all._

object Main {
  def main(args: Array[String]): Unit = {
    document.getElementById("Container").appendChild(
      h1("Hello, world!").render
    )
  }
}
