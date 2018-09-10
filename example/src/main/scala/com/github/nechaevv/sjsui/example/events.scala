package com.github.nechaevv.sjsui.example

trait AppEvent

case object AppStartEvent extends AppEvent
case class TaskSelectEvent(index: Int) extends AppEvent
case object TaskSaveEvent extends AppEvent
case class TaskEditNameEvent(name: String) extends AppEvent
case class TaskSetCompletedEvent(isCompleted: Boolean) extends AppEvent