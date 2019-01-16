package com.github.nechaevv.isomorphic.api

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal
class WeakMap[K, V] extends js.Object {
  def has(key: K): Boolean = js.native
  def get(key: K): js.UndefOr[V] = js.native
  def set(key: K, value: V): Unit = js.native
  def delete(key: K): Unit = js.native
}
