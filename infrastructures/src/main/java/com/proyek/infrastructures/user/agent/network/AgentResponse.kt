package com.proyek.infrastructures.user.agent.network

import com.google.gson.annotations.SerializedName
import com.proyek.infrastructures.user.agent.entities.Error

data class AgentResponse(
    val errors: Error,
    @SerializedName("message")
    val message: String,
    val data: AgentDataResponse
)