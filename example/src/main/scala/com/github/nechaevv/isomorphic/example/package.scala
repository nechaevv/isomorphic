package com.github.nechaevv.isomorphic

import scala.scalajs.js
import com.github.nechaevv.isomorphic.dom.{CustomElement, CustomElementDef}

package object example {
  val initialTasksState = TasksState(
    tasks = Seq(
      Task("Task 1", true),
      Task("Task 2", false)
    ),
    editingIndex = None,
    editingTask = Task("", false),
    message = None
  )

  def stateReducer: Reducer[AppEvent, TasksState] = {
    case TaskSelectEvent(taskIndex) ⇒
      s: TasksState ⇒ s.copy(editingTask = s.tasks(taskIndex), editingIndex = Some(taskIndex))
    case TaskEditNameEvent(name) ⇒
      s: TasksState ⇒ s.copy(editingTask = s.editingTask.copy(name = name))
    case TaskSetCompletedEvent(isCompleted) ⇒
      s: TasksState ⇒ s.copy(editingTask = s.editingTask.copy(completed = isCompleted))
    case TaskSaveEvent ⇒
      s: TasksState ⇒ s.copy(
        tasks = s.editingIndex.fold(s.tasks :+ s.editingTask)(idx ⇒ s.tasks.patch(idx, Seq(s.editingTask), 1)),
        editingTask = Task("", false),
        editingIndex = None
      )
    case ShowMessage(msg) ⇒ s => s.copy(message = Some(msg))
  }

  def appEffect: Effect[AppEvent, TasksState] = {
    case TaskSaveEvent ⇒ s ⇒ fs2.Stream(ShowMessage(s"Task ${s.tasks.length} saved"))
  }

  implicit val tasksAppElementDef = new CustomElementDef[TasksApp.type](js.constructorOf[TasksAppCustomElement])
  class TasksAppCustomElement extends CustomElement(TasksApp)
}