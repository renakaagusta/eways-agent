package com.eways.agent.order.activity.getcustomer.viewdto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InternetServiceOptionViewDTO (
    val id: String,
    val name:String,
    val description:String
): Parcelable