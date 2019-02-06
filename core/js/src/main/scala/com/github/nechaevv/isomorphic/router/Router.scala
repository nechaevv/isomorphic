package com.github.nechaevv.isomorphic.router

import org.scalajs.dom.raw.{History, Location}

import scala.scalajs.js

object Router {
  val history: History = js.window.history
  def currentRoute(route: Route)
  def navigate(route: Route)

  private def parseRoute(location: Location): Route = Route(
    location.pathname.split("/").toList,
    location.search.split("&").map(part ⇒ {
      val Array(k, v) = part.split("=")
      (k, v)
    }).toMap,
    if (location.hash.isEmpty) None else Some(location.hash.substring(1))
  )
}
