package com.github.nechaevv.isomorphic.example

case class TasksState
(
  tasks: Seq[Task],
  editingIndex: Option[Int],
  editingTask: Task,
  message: Option[String]
)
