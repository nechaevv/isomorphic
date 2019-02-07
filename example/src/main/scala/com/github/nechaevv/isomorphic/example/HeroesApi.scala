package com.github.nechaevv.isomorphic.example

import cats.effect.IO
import com.github.nechaevv.isomorphic.example.model.Hero

object HeroesApi {
  def loadHeroes: fs2.Stream[IO, Seq[Hero]] = fs2.Stream(heroesInitialSet)
}
