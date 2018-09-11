package com.github.nechaevv.sjsui.example

import com.github.nechaevv.sjsui.WebComponent
import org.scalajs.dom.raw.HTMLElement

class TasksApp extends WebComponent {
  override type Event = AppEvent
  override type State = TasksState
  override type Component = AppComponent.type

  override def rootComponent = AppComponent

  override def initialState = initialTasksState

  override def reducer = stateReducer

  override def initEvent(element: HTMLElement) = AppStartEvent
}
