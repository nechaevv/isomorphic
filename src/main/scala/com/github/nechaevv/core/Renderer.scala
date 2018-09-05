package com.github.nechaevv.core

trait Renderer[T] {
  def element(name: String, attributes: Map[String, String], children: Seq[T]): T
  def fragment(contents: T*): T
  def text(content: String): T
}
