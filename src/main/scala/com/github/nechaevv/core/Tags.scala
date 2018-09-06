package com.github.nechaevv.core

class Tags {
  type Tag[E,F] = (Map[String, String], Seq[E]) ⇒ Renderer[E,F] ⇒ E

  def tag[E,F](name: String)(attributes: Map[String, String], children: E*)(renderer: Renderer[E,F]): E = {
    renderer.element(name, attributes, children:_*)
  }

  def div[E,F]: Tag[E,F] = tag("div")
  def h1[E,F]: Tag[E,F] = tag("h1")
  def h2[E,F]: Tag[E,F] = tag("h2")

}
