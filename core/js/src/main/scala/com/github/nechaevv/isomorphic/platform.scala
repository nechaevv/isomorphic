package com.github.nechaevv.isomorphic

object platform extends UiPlatform
{

  override type Event = org.scalajs.dom.Event

  val functionCache: FunctionCache = WeakCache

}
