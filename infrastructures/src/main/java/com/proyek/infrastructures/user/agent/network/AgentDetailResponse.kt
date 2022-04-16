package com.proyek.infrastructures.user.agent.network

import com.google.gson.annotations.SerializedName
import com.proyek.infrastructures.user.agent.entities.Error
import com.proyek.infrastructures.user.agent.entities.UserAgent

data class AgentDetailResponse(
    val errors: Error,
    @SerializedName("message")
    val message: String,
    val data: UserAgent
)