package com.github.nechaevv.isomorphic

trait FunctionCache {
  def memoize[A, B](func: A ⇒ B): A ⇒ B
}
