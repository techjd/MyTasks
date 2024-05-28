package com.techjd.mytasks.data.remote.dto.response.success

import com.techjd.mytasks.domain.model.success.Success
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuccessDto(
    @SerialName("status")
    val status: String
)

fun SuccessDto.toSuccess(): Success {
    return Success(status)
}