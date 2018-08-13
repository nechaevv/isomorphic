package com.github.nechaevv.react

import com.github.nechaevv.Renderer

import scala.scalajs.js
import js.JSConverters._

object ReactRenderer extends Renderer[Element] {

  override def createElement(name: String, attributes: Seq[(String, String)], children: Seq[Element]): Element = {
    React.createElement(name, Map(attributes:_*), children:_*)
  }

  //override def createFragment(contents: Seq[Element]): Element = ???

}
