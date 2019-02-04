package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic.{AutonomousCustomElement, DomReconcilerRender, StatefulHostComponent}

object TasksApp extends StatefulHostComponent with AutonomousCustomElement with DomReconcilerRender {

  override type State = TasksState

  override def elementName: String = "app-tasks"

  override def rootComponent: AppComponent.type = AppComponent

  override def initialState(properties: Iterable[(String, String)]): State = initialTasksState

  override def reducer = stateReducer

  override def effect = appEffect

}