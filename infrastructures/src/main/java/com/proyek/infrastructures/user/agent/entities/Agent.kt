package com.proyek.infrastructures.user.agent.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Agent(
    @SerializedName("id")
    var id: Int? = null,

    @SerializedName("nik")
    var nik: String? = null,

    @SerializedName("user_Id")
    val user_id: String? = null,

    @SerializedName("cluster_id")
    val cluster_id: String? = null,

    @SerializedName("employeeId")
    var employee_id: String? = null
) : Parcelable