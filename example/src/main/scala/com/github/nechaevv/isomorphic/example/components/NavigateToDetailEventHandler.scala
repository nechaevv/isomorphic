package com.github.nechaevv.isomorphic.example.components

import cats.effect.IO
import com.github.nechaevv.isomorphic.example.NavigateToHeroDetail
import com.github.nechaevv.isomorphic.frontend.EventHandler
import org.scalajs.dom.Event

case class NavigateToDetailEventHandler(heroId: Int) extends EventHandler {
  override def apply(e: Event): fs2.Stream[IO, Any] = {
    e.preventDefault()
    fs2.Stream(NavigateToHeroDetail(heroId))
  }
}
