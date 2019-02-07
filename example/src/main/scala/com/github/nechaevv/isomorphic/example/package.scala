package com.github.nechaevv.isomorphic

import com.github.nechaevv.isomorphic.example.model.Hero
import com.github.nechaevv.isomorphic.webcomponent.DelegatedCustomElement

package object example {
  val heroesInitialSet: Seq[Hero] = Seq(
    Hero(11, "Mr. Nice"),
    Hero(12, "Narco"),
    Hero(13, "Bombasto"),
    Hero(14, "Celeritas"),
    Hero(15, "Magneta"),
    Hero(16, "RubberMan"),
    Hero(17, "Dynama"),
    Hero(18, "Dr IQ"),
    Hero(19, "Magma"),
    Hero(20,"Tornado")
  )

  class TasksAppStatefulHostCustomElement extends DelegatedCustomElement(HeroesApp.customElementDelegate)

}
