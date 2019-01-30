package com.github.nechaevv.isomorphic.api

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal

@js.native
@JSGlobal
class ReduxDevToolsExtension extends js.Object {
  def connect(options: js.Any): ReduxDevToolsConnection = js.native
  def disconnect(): Unit = js.native
}

@js.native
@JSGlobal
class ReduxDevToolsConnection extends js.Object {
  def send(action: js.Any, state: js.Any): Unit = js.native
  def subscribe(listener: js.Function1[js.Any, Unit]): Unit = js.native
  def unsubscribe(): Unit = js.native
  def init(state: js.Any): Unit = js.native
  def error(message: String): Unit = js.native
}

object ReduxDevToolsExtension {
  private val instance = js.Dynamic.global.window.__REDUX_DEVTOOLS_EXTENSION__
    .asInstanceOf[js.UndefOr[ReduxDevToolsExtension]]
  def isAvailable: Boolean = instance.isEmpty
  def apply(): ReduxDevToolsExtension = instance.get
}

