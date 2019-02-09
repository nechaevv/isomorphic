package com.github.nechaevv.isomorphic.example.components

import com.github.nechaevv.isomorphic.example.model.HeroDetailState
import com.github.nechaevv.isomorphic.example.{HeroDetailNameChange, HeroDetailSave, NavigateBack}
import com.github.nechaevv.isomorphic.vdom._
import com.github.nechaevv.isomorphic.vdom.browser._
import com.github.nechaevv.isomorphic.vdom.tags._
import org.scalajs.dom.raw.HTMLInputElement

object HeroDetailComponent extends Component[HeroDetailState, ElementVNode] {
  override def apply(state: HeroDetailState): ElementVNode = div(classes += "hero-detail",
    h2(state.hero.name.toUpperCase),
    div(span("id: "), state.hero.id.toString),
    div(
      label("name: ",
        input('placeholder := "name", 'value := state.heroName, DOMEventTypes.Change → nameChangeEventHandler)
      )
    ),
    button(DOMEventTypes.Click → backEventHandler, "go back"),
    button(DOMEventTypes.Click → saveEventHandler, "save"),
  )
  val nameChangeEventHandler: EventHandler = e ⇒ fs2.Stream(HeroDetailNameChange(e.target.asInstanceOf[HTMLInputElement].value))
  val saveEventHandler: EventHandler = e ⇒ fs2.Stream(HeroDetailSave)
  val backEventHandler: EventHandler = e ⇒ fs2.Stream(NavigateBack)
}
