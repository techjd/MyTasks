package com.techjd.mytasks.data.remote.dto.response.tasks


import com.google.gson.annotations.SerializedName
import com.techjd.mytasks.domain.model.tasks.Tasks

data class TasksDto(
    @SerializedName("tasks")
    val tasks: List<TaskDto>
)

fun TasksDto.toTasks(): Tasks {
    return Tasks(
      tasks = tasks.map {
          it.toTask()
      }
    )
}