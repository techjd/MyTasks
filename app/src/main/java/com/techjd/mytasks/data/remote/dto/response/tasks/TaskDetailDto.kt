package com.techjd.mytasks.data.remote.dto.response.tasks

import com.google.gson.annotations.SerializedName
import com.techjd.mytasks.domain.model.tasks.TaskDetail

data class TaskDetailDto(
  @SerializedName("date")
  val date: Long? = null,
  @SerializedName("description")
  val description: String? = null,
  @SerializedName("title")
  val title: String
)

fun TaskDetailDto.toTaskDetail(): TaskDetail {
  return TaskDetail(date, description, title)
}