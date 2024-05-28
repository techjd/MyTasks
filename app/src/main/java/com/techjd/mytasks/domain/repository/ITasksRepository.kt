package com.techjd.mytasks.domain.repository

import com.techjd.mytasks.data.NetworkResult
import com.techjd.mytasks.data.remote.dto.response.success.SuccessDto
import com.techjd.mytasks.data.remote.dto.response.tasks.TasksDto

interface ITasksRepository {

  suspend fun getAllTasks() : NetworkResult<TasksDto>

  suspend fun deleteTasks(taskId: Long) : NetworkResult<SuccessDto>

  suspend fun addTasks(title: String, date: Long, description: String?) : NetworkResult<SuccessDto>
}