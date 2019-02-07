package com.github.nechaevv.isomorphic.router

import org.scalajs.dom

import scala.scalajs.js

object Router {

  def currentRoute: String = dom.window.location.pathname

  def navigate(route: String): fs2.Stream[fs2.Pure, Any] = {
    dom.window.history.pushState(js.undefined, "", route)
    fs2.Stream(RouteChangeEvent(route))
  }

  def back(): fs2.Stream[fs2.Pure, Any] = {
    dom.window.history.back()
    fs2.Stream(RouteChangeEvent(currentRoute))
  }
}
