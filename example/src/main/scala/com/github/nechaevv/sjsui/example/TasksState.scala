package com.github.nechaevv.sjsui.example

case class TasksState
(
  tasks: Seq[Task],
  editingIndex: Option[Int],
  editingTask: Task
)
