package com.github.nechaevv.isomorphic

package object router {
//  def routerLifecycleEffect: Effect[Any] = {
//    case ComponentConnectedEvent ⇒
//  }
}

case class LocationChangedEvent(location: List[String])
case class ChangeLocationEvent(location: List[String])
