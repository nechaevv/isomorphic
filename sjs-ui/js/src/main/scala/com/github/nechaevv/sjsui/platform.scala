package com.github.nechaevv.sjsui

object platform extends UiPlatform
{

  override type Event = org.scalajs.dom.Event

  val functionCache: FunctionCache = WeakCache

}
