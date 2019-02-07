package com.github.nechaevv.isomorphic.example.model

import monocle.macros.Lenses

@Lenses
case class HeroesAppState
(
  route: String,
  heroes: Seq[Hero],
  detail: Option[HeroDetailState],
  search: HeroSearchState,
  newHeroName: String,
  messages: Seq[String]
)
