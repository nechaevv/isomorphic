package com.github.nechaevv.tasks

case class TasksState
(
  tasks: Seq[Task],
  editingIndex: Option[Int],
  editingTask: Task
)
