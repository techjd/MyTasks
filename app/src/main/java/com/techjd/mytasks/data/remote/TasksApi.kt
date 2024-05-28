package com.techjd.mytasks.data.remote

import com.techjd.mytasks.data.remote.dto.response.success.SuccessDto
import com.techjd.mytasks.data.remote.dto.request.alltask.AllTaskRequest
import com.techjd.mytasks.data.remote.dto.request.deletetask.DeleteTaskRequest
import com.techjd.mytasks.data.remote.dto.request.task.TaskRequest
import com.techjd.mytasks.data.remote.dto.response.tasks.TasksDto
import com.techjd.mytasks.util.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TasksApi {

  @POST(Constants.STORE_TASK)
  suspend fun storeTask(@Body taskRequest: TaskRequest): Response<SuccessDto>

  @POST(Constants.DELETE_TASK)
  suspend fun deleteTask(@Body deleteTaskRequest: DeleteTaskRequest): Response<SuccessDto>

  @POST(Constants.GET_TASKS)
  suspend fun getAllTasks(@Body allTaskRequest: AllTaskRequest): Response<TasksDto>
}