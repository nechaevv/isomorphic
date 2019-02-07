package com.github.nechaevv.isomorphic.example.components

import com.github.nechaevv.isomorphic.example._
import com.github.nechaevv.isomorphic.example.model.HeroesAppState
import com.github.nechaevv.isomorphic.frontend.DOMEventTypes
import com.github.nechaevv.isomorphic.vdom._
import com.github.nechaevv.isomorphic.vdom.browser._
import com.github.nechaevv.isomorphic.vdom.tags._

import scala.util.matching.Regex

object AppComponent extends Component[HeroesAppState, FragmentVNode] {
  override def apply(state: HeroesAppState): FragmentVNode = fragment(
    nav(
      a('href := "#", DOMEventTypes.Click → goDashboardEventHandler, "Dashboard"),
      a('href := "#", DOMEventTypes.Click → goHeroesEventHandler, "Heroes"),
    ),
    state.route match {
      case dashboardRoute(_*) ⇒ Some(DashboardComponent(state))
      case heroesRoute(_*) ⇒ Some(HeroesComponent(state))
      case detailRoute(_*) ⇒ state.detail.map(HeroDetailComponent(_))
      case _ ⇒ None
    },
    MessagesComponent(state.messages)
  )

  val dashboardRoute: Regex = "^/dashboard$".r
  val heroesRoute: Regex = "^/heroes$".r
  val detailRoute: Regex = "^/detail/[0-9]+$".r

  val goDashboardEventHandler: EventHandler = e ⇒ {
    e.preventDefault()
    fs2.Stream(NavigateToDashboard)
  }
  val goHeroesEventHandler: EventHandler = e ⇒ {
    e.preventDefault()
    fs2.Stream(NavigateToHeroes)
  }
}
