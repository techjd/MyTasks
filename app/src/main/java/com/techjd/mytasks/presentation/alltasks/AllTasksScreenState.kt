package com.techjd.mytasks.presentation.alltasks

import com.techjd.mytasks.domain.model.tasks.Tasks

data class AllTasksScreenState(
  val tasks: Tasks = Tasks(listOf()),
  val isLoading: Boolean = false,
  val filteredTasks: Tasks = Tasks(listOf()),
  val error: String? = null,
  val isHittingDeleteApi: Boolean = false
)
