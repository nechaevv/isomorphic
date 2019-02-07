package com.github.nechaevv.isomorphic

import com.github.nechaevv.isomorphic.example.model.Hero
import com.github.nechaevv.isomorphic.router.{RouteChangeEvent, Router}
import com.github.nechaevv.isomorphic.webcomponent.DelegatedCustomElement

package object example {
  val heroesInitialSet: Seq[Hero] = Seq(
      11 → "Mr. Nice",
      12 → "Narco",
      13 → "Bombasto",
      14 → "Celeritas",
      15 → "Magneta",
      16 → "RubberMan",
      17 → "Dynama",
      18 → "Dr IQ",
      19 → "Magma",
      20 → "Tornado"
  ).map(Hero.tupled)

  val appEffect = combineEffects[TasksState]({
    case TaskSaveEvent ⇒ s ⇒ fs2.Stream(ShowMessage(s"Task ${s.tasks.length} saved"))
    case NavigateToDashboard ⇒ _ ⇒ Router.navigate("/dashboard")
    case NavigateToHeroes ⇒ _ ⇒ Router.navigate("/heroes")
  })

  class TasksAppStatefulHostCustomElement extends DelegatedCustomElement(HeroesApp.customElementDelegate)

}
