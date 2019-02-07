package com.github.nechaevv.isomorphic.example.model

import monocle.macros.Lenses

@Lenses
case class Hero(id: Int, name: String)
