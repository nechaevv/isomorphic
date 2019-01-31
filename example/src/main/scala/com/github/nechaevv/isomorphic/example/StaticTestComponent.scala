package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic.dom
import com.github.nechaevv.isomorphic.dom.Node
import com.github.nechaevv.isomorphic.dom.dsl._
import com.github.nechaevv.isomorphic.dom.tags._

object StaticTestComponent extends dom.Component[Unit] {

  override def apply(v1: Unit): Node = fragment(
    div.props('class := "qwerty")(
      h1("Hello, World!"),
      h2("Hello again!")
    )
  )
}
