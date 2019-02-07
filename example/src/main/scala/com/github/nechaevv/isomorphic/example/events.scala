package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic.example.model.Hero

case class HeroesLoadEvent(heroes: Seq[Hero])

case class NavigateToHeroDetail(heroId: Int)

case class NewHeroNameChange(name: String)

case object SaveNewHero

case class DeleteHero(heroId: Int)

case object NavigateToDashboard

case class HeroDetailLoadEvent(heroDetail: Hero)

case object NavigateToHeroes

case object NavigateBack

case class SearchHeroes(search: String)

case class HeroesSearchResult(result: Seq[Hero])

case class HeroDetailNameChange(name: String)

case object HeroDetailSave

case class AddMessage(message: String)

case object ClearMessages
