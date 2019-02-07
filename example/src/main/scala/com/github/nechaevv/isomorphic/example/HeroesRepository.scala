package com.github.nechaevv.isomorphic.example

import cats.effect.IO
import com.github.nechaevv.isomorphic.example.model.Hero

object HeroesRepository {
  def getHeroes(): IO[Seq[Hero]] = IO { heroes }
  def getHero(id: Int): IO[Hero] = IO { heroes.find(_.id == id).get }
  def searchHeroes(search: String): IO[Seq[Hero]] = IO { heroes.filter(_.name.startsWith(search))}
  def saveHero(id: Int, name: String): IO[Hero] = IO {
    val heroIndex = heroes.indexWhere(_.id == id)
    if (heroIndex < 0) throw new NoSuchElementException
    val newHero = heroes(heroIndex).copy(name = name)
    heroes = heroes.map(h ⇒ if (h.id == id) newHero else h)
    newHero
  }
  def addHero(name: String): IO[Hero] = IO {
    val id = heroes.map(_.id).max + 1
    val newHero = Hero(id, name)
    heroes = heroes :+ newHero
    newHero
  }
  def deleteHero(id: Int): IO[Unit] = IO {
    heroes = heroes.filter(_.id != id)
  }

  private var heroes: Seq[Hero] = Seq(heroesInitialSet:_*)

}
