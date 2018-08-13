package com.github.nechaevv

object Tags {
  def h1(children: Component) = new Component[Unit] {
    override def render[T](state: Unit)(implicit renderer: Renderer[T]): T = renderer.createElement("h1")
  }
}
