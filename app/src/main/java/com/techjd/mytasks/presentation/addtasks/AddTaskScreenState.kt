package com.techjd.mytasks.presentation.addtasks

data class AddTaskScreenState(
  val isHittingApi: Boolean = false,
  val isSuccess: Boolean = false,
  val errorMessage: String? = null
)
