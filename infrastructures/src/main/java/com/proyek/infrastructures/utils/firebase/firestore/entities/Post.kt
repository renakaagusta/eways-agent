package com.proyek.infrastructures.utils.firebase.firestore.entities

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Post (

    @SerializedName("creator")
    var creatorId: String? = null,

    @SerializedName("content")
    var content: String? = null,

    @SerializedName("createdAt")
    var createdAt: Date? = null,

    @SerializedName("updatedAt")
    var updatedAt: Date? = null

) : Parcelable