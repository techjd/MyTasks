package com.techjd.mytasks

import kotlinx.serialization.Serializable

@Serializable
object AllTasks

@Serializable
data class AddTask(
  val day: Int,
  val month: Int,
  val year: Int
)