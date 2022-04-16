package com.proyek.infrastructures.kabarcluster.entities

import com.google.gson.annotations.SerializedName
data class Post (
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("user_id")
    val user_id: String? = null,

    @SerializedName("content")
    val content: String? = null,

    @SerializedName("pinned")
    val pinned: Int? = null,

    @SerializedName("cluster_id")
    val cluster_id: String? = null,

    @SerializedName("created_at")
    val created_at: String? = null,

    @SerializedName("updated_at")
    val updated_at: String? = null
)