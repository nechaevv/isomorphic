package com.github.nechaevv.isomorphic.example

case class TaskSelectEvent(index: Int)

case object TaskSaveEvent

case class TaskEditNameEvent(name: String)

case class TaskSetCompletedEvent(isCompleted: Boolean)

case class ShowMessage(message: String)

case class TaskDeleteEvent(index: Int)

case object NavigateToDashboard

case object NavigateToHeroes
