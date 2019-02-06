package com.github.nechaevv.isomorphic

import com.github.nechaevv.isomorphic.router.{RouteChangeEvent, Router}
import com.github.nechaevv.isomorphic.webcomponent.DelegatedCustomElement

package object example {
  val initialTasksState = TasksState(
    route = Router.currentRoute,
    tasks = Seq(
      Task("Task 1", completed = true),
      Task("Task 2", completed = false)
    ),
    editingIndex = None,
    editingTask = Task("", completed = false),
    message = None
  )

  val stateReducer = combineReducers[TasksState]({
    case RouteChangeEvent(route) ⇒ TasksState.route.set(route)
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
    case TaskDeleteEvent(index) ⇒ TasksState.tasks.modify(tasks ⇒ tasks.take(index)
      ++ (if (index < tasks.length - 1) tasks.slice(index + 1, tasks.length) else Nil))
    case ShowMessage(msg) ⇒ TasksState.message.set(Some(msg))
  })

  val appEffect = combineEffects[TasksState]({
    case TaskSaveEvent ⇒ s ⇒ fs2.Stream(ShowMessage(s"Task ${s.tasks.length} saved"))
    case NavigateToDashboard ⇒ _ ⇒ Router.navigate("/dashboard")
  })

  class TasksAppStatefulHostCustomElement extends DelegatedCustomElement(TasksApp.customElementDelegate)

}
