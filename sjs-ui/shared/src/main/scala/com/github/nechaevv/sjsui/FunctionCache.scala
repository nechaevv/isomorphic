package com.github.nechaevv.sjsui

trait FunctionCache {
  def memoize[A,B](func: A ⇒ B): A ⇒ B
}
