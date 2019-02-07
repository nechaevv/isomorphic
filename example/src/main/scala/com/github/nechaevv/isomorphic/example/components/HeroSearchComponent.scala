package com.github.nechaevv.isomorphic.example.components

import com.github.nechaevv.isomorphic.example.SearchHeroes
import com.github.nechaevv.isomorphic.example.model.HeroSearchState
import com.github.nechaevv.isomorphic.frontend.DOMEventTypes
import com.github.nechaevv.isomorphic.vdom._
import com.github.nechaevv.isomorphic.vdom.browser._
import com.github.nechaevv.isomorphic.vdom.tags._
import org.scalajs.dom.raw.HTMLInputElement

object HeroSearchComponent extends Component[HeroSearchState, ElementVNode] {
  override def apply(searchState: HeroSearchState): ElementVNode = div(classes += "hero-search",
    h4("Hero Search"),
    input('value := searchState.searchBox , DOMEventTypes.Input → searchInputEventHandler),
    ul(classes += "search-result",
      for(hero ← searchState.searchResults) yield li.withKey(hero.id.toString)(
        a('href := "#", DOMEventTypes.Click → NavigateToDetailEventHandler(hero.id), hero.name)
      )
    )
  )

  val searchInputEventHandler: EventHandler = e ⇒ fs2.Stream(SearchHeroes(e.target.asInstanceOf[HTMLInputElement].value))

}
