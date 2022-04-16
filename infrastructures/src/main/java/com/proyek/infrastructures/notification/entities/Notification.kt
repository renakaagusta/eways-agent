package com.proyek.infrastructures.notification.entities

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("id")
    var id: String? = null,

    @SerializedName("receiver_id")
    var receiver: String? = null,


    @SerializedName("type")
    val type: Int? = null,

    @SerializedName("type_id")
    val typeId: String? = null,

    @SerializedName("title")
    var title: String? = null,

    @SerializedName("description")
    var description: String? = null,

    @SerializedName("read_at")
    var readAt: String? = null,

    @SerializedName("created_at")
    var createdAt: String? = null,

    @SerializedName("updated_at")
    var updatedAt: String? = null
)