package com.proyek.infrastructures.user.cluster.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClusterArea(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("cluster_id")
    val clusterId: String? = null,

    @SerializedName("area")
    val area: String? = null
) : Parcelable