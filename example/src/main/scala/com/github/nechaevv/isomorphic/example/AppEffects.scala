package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic.Effect
import com.github.nechaevv.isomorphic.example.model.HeroesAppState
import com.github.nechaevv.isomorphic.router.{RouteChangeEvent, Router}
import com.github.nechaevv.isomorphic.webcomponent.ComponentConnectedEvent

import scala.util.matching.Regex

object AppEffects {
  val detailRoute: Regex = "^/detail/([0-9]+)$".r

  val effect: Effect[HeroesAppState] = {
    case ComponentConnectedEvent ⇒ _ ⇒ fs2.Stream(HeroesLoadEvent(heroesInitialSet))
    case RouteChangeEvent(detailRoute(heroIdStr)) ⇒ s ⇒
      val heroId = heroIdStr.toInt
      s.heroes.find(_.id == heroId).fold[fs2.Stream[fs2.Pure, HeroDetailLoadEvent]](fs2.Stream.empty)(hero ⇒ fs2.Stream(HeroDetailLoadEvent(hero)))
    case NavigateToDashboard ⇒ _ ⇒ Router.navigate(s"/dashboard")
    case NavigateToHeroes ⇒ _ ⇒ Router.navigate(s"/heroes")
    case NavigateToHeroDetail(heroId) ⇒ _ ⇒ Router.navigate(s"/detail/$heroId")
    case NavigateBack ⇒ _ ⇒ Router.back()
    case SearchHeroes(search) ⇒ s ⇒ fs2.Stream(if (search.length > 2) s.heroes.filter(_.name.startsWith(search)) else Nil)
  }

}
