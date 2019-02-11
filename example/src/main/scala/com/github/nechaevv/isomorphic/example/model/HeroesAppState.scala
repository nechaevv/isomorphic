package com.github.nechaevv.isomorphic.example.model

import com.github.nechaevv.isomorphic.router.Route
import monocle.macros.Lenses

@Lenses
case class HeroesAppState
(
  route: Route,
  heroes: Seq[Hero],
  detail: Option[HeroDetailState],
  search: HeroSearchState,
  newHeroName: String,
  messages: Seq[String]
)
