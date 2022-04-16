package com.proyek.infrastructures.user.user.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(

    @SerializedName("id")
    var ID: String? = null,

    @SerializedName("userName")
    var username: String? = null,

    @SerializedName("fullName")
    var fullname: String? = null,

    @SerializedName("phoneNumber")
    var phoneNumber: String? = null,

    @SerializedName("email")
    var email: String? = null,

    @SerializedName("password")
    var password: String? = null,

    @SerializedName("imagePath")
    var imagePath: String? = null,

    @SerializedName("role")
    var role: Int? = null,

    @SerializedName("activeStatus")
    var activeStatus: Int? = null,

    @SerializedName("verifStatus")
    var verifStatus: Int? = null,

    @SerializedName("address")
    var address: String? = null,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null
) : Parcelable