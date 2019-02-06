package com.github.nechaevv.isomorphic.router

case class Route(path: List[String], query: Map[String, String], hash: Option[String])
