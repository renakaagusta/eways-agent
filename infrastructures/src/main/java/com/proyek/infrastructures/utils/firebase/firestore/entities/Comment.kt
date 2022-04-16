package com.proyek.infrastructures.utils.firebase.firestore.entities

import com.google.gson.annotations.SerializedName
import java.util.*

data class Comment(
    @SerializedName("creator")
    val creatorId: String? = null,

    @SerializedName("content")
    val content: String? = null,

    @SerializedName("createdAt")
    val createdAt: Date? = null,

    @SerializedName("updatedAt")
    val updatedAt: Date? = null
)