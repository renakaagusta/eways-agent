package com.proyek.infrastructures.user.customer.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CustomerUser(

    @SerializedName("id")
    var ID: String? = null,

    @SerializedName("user_name")
    var username: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("full_name")
    var fullname: String? = null,

    @SerializedName("phone_number")
    var phoneNumber: String? = null,

    @SerializedName("email")
    var email: String? = null,

    @SerializedName("password")
    var password: String? = null,

    @SerializedName("image_path")
    var imagePath: String? = null,

    @SerializedName("role")
    var role: Int? = null,

    @SerializedName("active_status")
    var activeStatus: Int? = null,

    @SerializedName("address")
    val address: String? = null,

    @SerializedName("latitude")
    var addressLat: Float? = null,

    @SerializedName("longitude")
    var addressLng: Float? = null,

    @SerializedName("cluster_id")
    var clusterId: String? = null,

    @SerializedName("cluster")
    var cluster: String? = null,

    @SerializedName("verif_status")
    val verifStatus: Int? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null,

    @SerializedName("user")
    val user: UserCustomer? = null

) : Parcelable