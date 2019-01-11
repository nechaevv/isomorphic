package com.github.nechaevv.isomorphic

import scala.scalajs.js
import scala.scalajs.js.Dynamic.global

class WeakCache extends FunctionCache {
  private val weakMap = js.Dynamic.newInstance(global.WeakMap)()

  override def memoize[A, B](func: A ⇒ B): A ⇒ B = { a: A ⇒
    if (!js.isUndefined(a) && (a.isInstanceOf[js.Object] || a.isInstanceOf[AnyRef])) {
      val cachedValue = weakMap.get(a.asInstanceOf[js.Any])
      if (js.isUndefined(cachedValue)) {
        val newValue = func(a)
        weakMap.set(newValue.asInstanceOf[js.Any])
        newValue
      } else cachedValue.asInstanceOf[B]
    } else func(a)
  }

}
