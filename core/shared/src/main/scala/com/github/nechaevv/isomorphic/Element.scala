package com.github.nechaevv.isomorphic

trait Element {
  def apply[E](renderer: Renderer[E]): E
}
