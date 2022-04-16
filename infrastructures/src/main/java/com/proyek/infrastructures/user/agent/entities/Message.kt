package com.proyek.infrastructures.user.agent.entities

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("message")
    val message: String
)