package com.techjd.mytasks.data.repository

import com.techjd.mytasks.data.NetworkResult
import com.techjd.mytasks.data.handleApi
import com.techjd.mytasks.data.remote.TasksApi
import com.techjd.mytasks.data.remote.dto.request.alltask.AllTaskRequest
import com.techjd.mytasks.data.remote.dto.request.deletetask.DeleteTaskRequest
import com.techjd.mytasks.data.remote.dto.request.task.Task
import com.techjd.mytasks.data.remote.dto.request.task.TaskRequest
import com.techjd.mytasks.data.remote.dto.response.success.SuccessDto
import com.techjd.mytasks.data.remote.dto.response.tasks.TasksDto
import com.techjd.mytasks.domain.repository.ITasksRepository
import com.techjd.mytasks.util.Constants
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
  private val tasksApi: TasksApi
) : ITasksRepository {
  override suspend fun getAllTasks(): NetworkResult<TasksDto> {
    return handleApi {
      tasksApi.getAllTasks(AllTaskRequest(Constants.USER_ID))
    }
  }

  override suspend fun deleteTasks(taskId: Long): NetworkResult<SuccessDto> {
    return handleApi {
      tasksApi.deleteTask(DeleteTaskRequest(taskId, Constants.USER_ID))
    }
  }

  override suspend fun addTasks(
    title: String,
    date: Long,
    description: String?
  ): NetworkResult<SuccessDto> {
    return handleApi {
      tasksApi.storeTask(TaskRequest(Task(date, description, title), Constants.USER_ID))
    }
  }
}