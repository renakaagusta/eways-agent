package com.proyek.infrastructures.user.agent.entities

import com.google.gson.annotations.SerializedName

data class Error(
    @SerializedName("message")
    val message: List<String>? = null
)