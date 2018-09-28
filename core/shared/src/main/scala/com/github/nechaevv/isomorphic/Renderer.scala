package com.github.nechaevv.isomorphic

trait Renderer[E] {
  def element(name: String, modifiers: ElementModifier*): E
  def fragment(contents: E*): E
  def text(content: String): E
}
