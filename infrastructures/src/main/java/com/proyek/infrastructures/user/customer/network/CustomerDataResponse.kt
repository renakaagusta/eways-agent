package com.proyek.infrastructures.user.customer.network

import com.google.gson.annotations.SerializedName
import com.proyek.infrastructures.user.customer.entities.UserCustomer

data class CustomerDataResponse(
    @SerializedName("user")
    val user: UserCustomer
)