package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic.AutonomousCustomElement
import com.github.nechaevv.isomorphic.router.RouterSupport
import com.github.nechaevv.isomorphic.webcomponent.{DomReconcilerRender, StatefulHostComponent, StatefulHostElementDelegate}
import org.scalajs.dom.raw.HTMLElement

object TasksApp extends StatefulHostComponent with AutonomousCustomElement with DomReconcilerRender {

  override type State = TasksState

  override def elementName: String = "app-tasks"

  override def rootComponent: AppComponent.type = AppComponent

  override def initialState(properties: Iterable[(String, String)]): State = initialTasksState

  override def customElementDelegate(componentHost: HTMLElement): StatefulHostElementDelegate = {
    new StatefulHostElementDelegate(this, componentHost) with RouterSupport
  }

  override def reducer = stateReducer

  override def effect = appEffect

}
