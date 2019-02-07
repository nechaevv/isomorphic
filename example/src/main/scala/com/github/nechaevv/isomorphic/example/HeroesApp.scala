package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic.{AutonomousCustomElement, EventStream, combineEffects, combineReducers}
import com.github.nechaevv.isomorphic.example.components.AppComponent
import com.github.nechaevv.isomorphic.example.model.{HeroSearchState, HeroesAppState}
import com.github.nechaevv.isomorphic.router.{Router, RouterSupport}
import com.github.nechaevv.isomorphic.webcomponent.{DomReconcilerRender, StatefulHostComponent, StatefulHostElementDelegate}
import org.scalajs.dom.raw.HTMLElement

object HeroesApp extends StatefulHostComponent with AutonomousCustomElement with DomReconcilerRender {

  override type State = HeroesAppState

  override def elementName: String = "app-heroes"

  override def rootComponent: AppComponent.type = AppComponent

  override def initialState(properties: Iterable[(String, String)]): State = HeroesAppState(
    route = Router.currentRoute,
    heroes = Nil,
    detail = None,
    search = HeroSearchState("", Nil),
    newHeroName = "",
    messages = Nil
  )

  override def customElementDelegate(componentHost: HTMLElement): StatefulHostElementDelegate = {
    new StatefulHostElementDelegate(this, componentHost) with RouterSupport
  }

  override val reducer: Any ⇒ HeroesAppState ⇒ HeroesAppState = combineReducers(AppReducers.reducer)

  override def effect: Any ⇒ HeroesAppState ⇒ EventStream = combineEffects(AppEffects.effect)

}
