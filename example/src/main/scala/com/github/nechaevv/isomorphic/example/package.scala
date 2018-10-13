package com.github.nechaevv.isomorphic

import scala.scalajs.js

package object example {
  val initialTasksState = TasksState(
    tasks = Seq(
      Task("Task 1", completed = true),
      Task("Task 2", completed = false)
    ),
    editingIndex = None,
    editingTask = Task("", completed = false),
    message = None
  )

  def stateReducer: Reducer[TasksState] = {
    case TaskSelectEvent(taskIndex) ⇒ s ⇒
         s.copy(editingTask = s.tasks(taskIndex), editingIndex = Some(taskIndex))
    case TaskEditNameEvent(name) ⇒ (TasksState.editingTask composeLens Task.name).set(name)
    case TaskSetCompletedEvent(isCompleted) ⇒ (TasksState.editingTask composeLens Task.completed).set(isCompleted)
    case TaskSaveEvent ⇒
      s: TasksState ⇒ s.copy(
        tasks = s.editingIndex.fold(s.tasks :+ s.editingTask)(idx ⇒ s.tasks.patch(idx, Seq(s.editingTask), 1)),
        editingTask = Task("", completed = false),
        editingIndex = None
      )
    case ShowMessage(msg) ⇒ TasksState.message.set(Some(msg))
  }

  def appEffect: Effect[TasksState] = {
    case TaskSaveEvent ⇒ s ⇒ fs2.Stream(ShowMessage(s"Task ${s.tasks.length} saved"))
  }

  class TasksAppStatefulHostCustomElement extends StatefulHostCustomElement(TasksApp)

}
