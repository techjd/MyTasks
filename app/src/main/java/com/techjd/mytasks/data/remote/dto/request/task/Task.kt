package com.techjd.mytasks.data.remote.dto.request.task

import com.google.gson.annotations.SerializedName

data class Task(
    @SerializedName("date")
    val date: Long,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("title")
    val title: String
)