package com.proyek.infrastructures.user.cluster.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Cluster (
    @SerializedName("id")
    var ID: String? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("areas")
    var areas: List<ClusterAreas>? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null
) : Parcelable