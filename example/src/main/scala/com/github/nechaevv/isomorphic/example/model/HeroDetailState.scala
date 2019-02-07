package com.github.nechaevv.isomorphic.example.model

import monocle.macros.Lenses

@Lenses
case class HeroDetailState
(
  hero: Hero,
  heroName: String
)
