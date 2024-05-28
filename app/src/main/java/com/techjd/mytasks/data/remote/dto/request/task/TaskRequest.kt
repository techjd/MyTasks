package com.techjd.mytasks.data.remote.dto.request.task


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

data class TaskRequest(
    @SerializedName("task")
    val task: Task,
    @SerializedName("user_id")
    val userId: Int
)