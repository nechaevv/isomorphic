package com.github.nechaevv.isomorphic.example

import com.github.nechaevv.isomorphic.{Reducer, WebComponent}

object TasksApp extends WebComponent {

  override type Event = AppEvent
  override type State = TasksState

  override def useShadowRoot: Boolean = false

  override def tagName: String = "app-tasks"

  override def rootComponent: AppComponent.type = AppComponent

  override def initialState: State = initialTasksState

  override def reducer: Reducer[Event, State] = stateReducer

  override def initEvent(properties: Iterable[(String, String)]): Event = AppStartEvent

}