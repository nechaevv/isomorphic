package com.github.nechaevv.isomorphic.router

import org.scalajs.dom
import org.scalajs.dom.Location

import scala.scalajs.js

object Router {

  def currentRoute: Route = parseLocation(dom.window.location)

  def navigate(route: Route): fs2.Stream[fs2.Pure, Any] = {
    dom.window.history.pushState(js.undefined, "", route.toUrlString)
    fs2.Stream(RouteChangeEvent(route))
  }

  def back(): fs2.Stream[fs2.Pure, Any] = {
    dom.window.history.back()
    fs2.Stream(RouteChangeEvent(currentRoute))
  }

  def parseLocation(location: Location): Route = Route(
    location.pathname,
    if (location.search.startsWith("?")) Some(location.search.substring(1)) else None,
    if (location.hash.startsWith("#")) Some(location.hash.substring(1)) else None
  )

}
