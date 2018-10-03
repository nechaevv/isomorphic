package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic.{AutonomousWebComponent, Effect, ReactiveHostComponent, Reducer}

object TasksApp extends ReactiveHostComponent with AutonomousWebComponent {

  override type State = TasksState

  override def useShadowRoot: Boolean = false

  override def elementName: String = "app-tasks"

  override def rootComponent: AppComponent.type = AppComponent

  override def initialState(properties: Iterable[(String, String)]): State = initialTasksState

  override def reducer: Reducer[State] = stateReducer

  override def effect: Effect[TasksState] = appEffect

}