package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic.dom.Node
import com.github.nechaevv.isomorphic.dom.dsl._
import com.github.nechaevv.isomorphic.dom.tags._

object StaticTestComponent {
  def apply(): Seq[Node] = Seq(
    div.props('class := "qwerty").children(
      h1.children("Hello, World!"),
      h2.children("Hello again!")
    )
  )
}
