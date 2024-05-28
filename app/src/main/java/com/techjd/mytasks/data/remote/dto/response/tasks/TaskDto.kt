package com.techjd.mytasks.data.remote.dto.response.tasks


import com.google.gson.annotations.SerializedName
import com.techjd.mytasks.domain.model.tasks.Task

data class TaskDto(
    @SerializedName("task_detail")
    val taskDetail: TaskDetailDto,
    @SerializedName("task_id")
    val taskId: Long
)

fun TaskDto.toTask(): Task {
    return Task(
      taskDetail = taskDetail.toTaskDetail(),
      taskId = taskId
    )
}