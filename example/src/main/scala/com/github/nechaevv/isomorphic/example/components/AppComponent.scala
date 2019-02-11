package com.github.nechaevv.isomorphic.example.components

import com.github.nechaevv.isomorphic.example._
import com.github.nechaevv.isomorphic.example.model.HeroesAppState
import com.github.nechaevv.isomorphic.router.Route
import com.github.nechaevv.isomorphic.vdom._
import com.github.nechaevv.isomorphic.vdom.browser._
import com.github.nechaevv.isomorphic.vdom.tags._

import AppRoutes._

object AppComponent extends Component[HeroesAppState, FragmentVNode] {
  override def apply(state: HeroesAppState): FragmentVNode = fragment(
    nav(
      a('href := "#", DOMEventTypes.Click → goDashboardEventHandler, "Dashboard"),
      a('href := "#", DOMEventTypes.Click → goHeroesEventHandler, "Heroes"),
    ),
    state.route match {
      case Route(dashboardRoute(_*), _, _) ⇒ Some(DashboardComponent << state)
      case Route(heroesRoute(_*), _, _) ⇒ Some(HeroesComponent << state)
      case Route(detailRoute(_*), _, _) ⇒ state.detail.map(HeroDetailComponent << _)
      case _ ⇒ None
    },
    MessagesComponent << state.messages
  )

  val goDashboardEventHandler: EventHandler = e ⇒ {
    e.preventDefault()
    fs2.Stream(NavigateToDashboard)
  }
  val goHeroesEventHandler: EventHandler = e ⇒ {
    e.preventDefault()
    fs2.Stream(NavigateToHeroes)
  }
}
