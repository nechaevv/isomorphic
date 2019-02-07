package com.github.nechaevv.isomorphic.example.model

import monocle.macros.Lenses

@Lenses
case class HeroSearchState
(
  searchBox: String,
  searchResults: Seq[Hero]
)
