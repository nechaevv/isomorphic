package com.github.nechaevv.isomorphic.router

case class Route(path: String, query: Option[String] = None, hash: Option[String] = None) {
  def toUrlString: String = path + query.fold("")("?" + _) + hash.fold("")("#" + _)
}
