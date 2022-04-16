package com.proyek.infrastructures.user.cluster.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Area (
    @SerializedName("id")
    var ID: String? = null,

    @SerializedName("cluster_id")
    var clusterId: String? = null,

    @SerializedName("area")
    var area: String? = null,

    @SerializedName("created_t")
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null
) : Parcelable