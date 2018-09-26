package com.github.nechaevv.isomorphic

object platform extends UiPlatform {
  override type Event = Any
  override val functionCache: FunctionCache = new FunctionCache {
    override def memoize[A, B](func: A ⇒ B): A ⇒ B = func
  }
}
