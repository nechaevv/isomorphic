package com.github.nechaevv.isomorphic.example.components

import com.github.nechaevv.isomorphic.example.model.HeroesAppState
import com.github.nechaevv.isomorphic.vdom._
import com.github.nechaevv.isomorphic.frontend.DOMEventTypes
import browser._
import tags._


object DashboardComponent extends Component[HeroesAppState, FragmentVNode] {
  override def apply(state: HeroesAppState): FragmentVNode = fragment(
    h3("Top heroes"),
    div(classes += "grid grid-pad")(
      for (hero ← state.heroes.take(4)) yield a('href := "#", classes += "col-1-4", DOMEventTypes.Click → NavigateToDetailEventHandler(hero.id),
        div(classes += "module hero",
          h4(hero.name)
        )
      )
    )
  )
}
