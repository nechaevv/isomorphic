package com.github.nechaevv.isomorphic.example

import monocle.macros.Lenses

@Lenses
case class Task(name: String, completed: Boolean)
