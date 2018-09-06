package com.github.nechaevv.core

import scala.scalajs.js
import scala.scalajs.js.Dynamic.global

object CachingComponent {
  private val componentCache = js.Dynamic.newInstance(global.WeakMap)()

  def apply[S](component: Component[S]): Component[S] = new Component[S] {
    override def apply[E](state: S)(implicit renderer: Renderer[E]): E = {
      val cachedValue = componentCache.get(state.asInstanceOf[js.Any])
      if (js.isUndefined(cachedValue)) {
        val newValue = component(state)
        componentCache.set(newValue.asInstanceOf[js.Any])
        newValue
      } else cachedValue.asInstanceOf[E]
    }
  }

}
