package com.techjd.mytasks.data.remote.dto.request.alltask


import com.google.gson.annotations.SerializedName

data class AllTaskRequest(
    @SerializedName("user_id")
    val userId: Int
)