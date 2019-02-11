package com.github.nechaevv.isomorphic.example

import scala.util.matching.Regex

object AppRoutes {
  val dashboardRoute: Regex = "^/dashboard$".r
  val heroesRoute: Regex = "^/heroes$".r
  val detailRoute: Regex = "^/detail/([0-9]+)$".r
}
