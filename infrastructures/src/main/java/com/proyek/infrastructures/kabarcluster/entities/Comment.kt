package com.proyek.infrastructures.kabarcluster.entities

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("user_id")
    val user_id: String? = null,

    @SerializedName("post_id")
    val post_id: String? = null,

    @SerializedName("content")
    val content: String? = null,

    @SerializedName("created_at")
    val created_at: String? = null,

    @SerializedName("updated_at")
    val updated_at: String? = null
)