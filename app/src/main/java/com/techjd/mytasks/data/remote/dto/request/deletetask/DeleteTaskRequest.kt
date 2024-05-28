package com.techjd.mytasks.data.remote.dto.request.deletetask

import com.google.gson.annotations.SerializedName

data class DeleteTaskRequest(
    @SerializedName("task_id")
    val taskId: Long,
    @SerializedName("user_id")
    val userId: Int
)