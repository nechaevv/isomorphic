package com.github.nechaevv.sjsui.example

import com.github.nechaevv.sjsui.{Reducer, WebComponent}

object TasksApp extends WebComponent {

  override type Event = AppEvent
  override type State = TasksState
  override type Component = AppComponent.type

  override def useShadowRoot: Boolean = false

  override def tagName: String = "app-tasks"

  override def rootComponent: Component = AppComponent

  override def initialState: State = initialTasksState

  override def reducer: Reducer[Event, State] = stateReducer

  override def initEvent(properties: Iterable[(String, String)]): Event = AppStartEvent

}