package com.github.nechaevv.isomorphic.example

import cats.effect.IO
import com.github.nechaevv.isomorphic.Effect
import com.github.nechaevv.isomorphic.example.model.HeroesAppState
import com.github.nechaevv.isomorphic.router.{Route, RouteChangeEvent, Router}
import com.github.nechaevv.isomorphic.webcomponent.ComponentConnectedEvent

import AppRoutes._

import scala.language.implicitConversions

object AppEffects {
  implicit private def ioToStream[T](io: IO[T]): fs2.Stream[IO, T] = fs2.Stream.eval(io)

  val effect: Effect[HeroesAppState] = {
    case ComponentConnectedEvent ⇒ s ⇒ {
      (if (s.route.path == "/") Router.navigate(Route("/dashboard")) else fs2.Stream.empty) ++
      HeroesRepository.getHeroes().map(HeroesLoadEvent)
    }
    case RouteChangeEvent(Route(detailRoute(heroIdStr), _, _)) ⇒ s ⇒ HeroesRepository.getHero(heroIdStr.toInt)
      .map(HeroDetailLoadEvent)
    case NavigateToDashboard ⇒ _ ⇒ Router.navigate(Route("/dashboard"))
    case NavigateToHeroes ⇒ _ ⇒ Router.navigate(Route("/heroes"))
    case NavigateToHeroDetail(heroId) ⇒ _ ⇒ Router.navigate(Route(s"/detail/$heroId"))
    case NavigateBack ⇒ _ ⇒ Router.back()
    case SearchHeroes(search) ⇒ s ⇒ fs2.Stream(if (search.length > 2) {
      fs2.Stream.eval(HeroesRepository.searchHeroes(search)) ++
        fs2.Stream(AddMessage(s"""found heroes matching "$search""""))
    } else Nil)
    case SaveNewHero ⇒ s ⇒  fs2.Stream.eval(HeroesRepository.addHero(s.newHeroName))
      .flatMap(newHero ⇒ fs2.Stream.eval(HeroesRepository.getHeroes().map(HeroesLoadEvent)) ++
        fs2.Stream(AddMessage(s"added hero w/ id=${newHero.id}"), NewHeroNameChange("")))
    case HeroDetailSave ⇒ s ⇒ s.detail.fold[fs2.Stream[IO, Any]](fs2.Stream.empty)(detailState ⇒
      fs2.Stream.eval(HeroesRepository.saveHero(detailState.hero.id, detailState.heroName))
        .flatMap(newHero ⇒ fs2.Stream.eval(HeroesRepository.getHeroes().map(HeroesLoadEvent)) ++
        fs2.Stream(AddMessage(s"updated hero id=${newHero.id}")) ++
          Router.back()))
    case DeleteHero(id) ⇒ s => fs2.Stream.eval(HeroesRepository.deleteHero(id).flatMap(_ ⇒ HeroesRepository.getHeroes()
      .map(HeroesLoadEvent))) ++ fs2.Stream(AddMessage(s"deleted hero id=$id"))
  }

}
