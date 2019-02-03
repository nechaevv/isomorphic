package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic.dom
import com.github.nechaevv.isomorphic.dom.{FragmentNode, Node}
import com.github.nechaevv.isomorphic.dom.dsl._
import com.github.nechaevv.isomorphic.dom.tags._

object StaticTestComponent extends dom.Component[Unit, FragmentNode] {

  override def apply(v1: Unit): FragmentNode = fragment(
    div.attr('class := "qwerty")(
      h1("Hello, World!"),
      h2("Hello again!")
    )
  )
}
