package com.github.nechaevv.isomorphic.example.components

import cats.effect.IO
import com.github.nechaevv.isomorphic.example.{DeleteHero, NewHeroNameChange, SaveNewHero}
import com.github.nechaevv.isomorphic.example.model.HeroesAppState
import com.github.nechaevv.isomorphic.frontend.DOMEventTypes
import com.github.nechaevv.isomorphic.vdom._
import com.github.nechaevv.isomorphic.vdom.browser._
import com.github.nechaevv.isomorphic.vdom.tags._
import org.scalajs.dom.Event
import org.scalajs.dom.raw.HTMLInputElement

object HeroesComponent extends Component[HeroesAppState, ElementVNode] {
  override def apply(state: HeroesAppState): ElementVNode = div(classes += "heroes-list",
    h2("My Heroes"),
    div(
      label("Hero name: ",
        input('value := state.newHeroName, DOMEventTypes.Change → nameChangeEventHandler)
      ),
      button(DOMEventTypes.Click → saveEventHandler, "add")
    ),
    ul(classes += "heroes",
      for (hero ← state.heroes) yield li.withKey(hero.id.toString)(
        a('href := "#", DOMEventTypes.Click → NavigateToDetailEventHandler(hero.id),
          span(classes += "badge", hero.id.toString),
          hero.name
        ),
        button(classes += "delete", 'title := "delete hero", DOMEventTypes.Click → DeleteEventHandler(hero.id), "x")
      )
    )
  )
  val nameChangeEventHandler: EventHandler = e ⇒ fs2.Stream(NewHeroNameChange(e.target.asInstanceOf[HTMLInputElement].value))
  val saveEventHandler: EventHandler = e ⇒ fs2.Stream(SaveNewHero)
  case class DeleteEventHandler(heroId: Int) extends EventHandler {
    override def apply(e: Event): fs2.Stream[IO, Any] = fs2.Stream(DeleteHero(heroId))
  }
}
