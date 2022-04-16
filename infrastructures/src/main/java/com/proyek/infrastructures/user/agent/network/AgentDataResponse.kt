package com.proyek.infrastructures.user.agent.network

import com.google.gson.annotations.SerializedName
import com.proyek.infrastructures.user.agent.entities.UserAgent

data class AgentDataResponse(
    @SerializedName("user")
    val user: UserAgent,
    @SerializedName("token")
    val token: String
)