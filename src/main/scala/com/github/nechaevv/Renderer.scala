package com.github.nechaevv

trait Renderer[T] {
  def createElement(name: String, attributes: Seq[(String, String)], children: Seq[T]): T
  //def createFragment(contents: Seq[T]): T
}