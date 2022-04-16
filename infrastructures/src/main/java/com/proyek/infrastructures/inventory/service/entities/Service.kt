package com.proyek.infrastructures.inventory.service.entities

import com.google.gson.annotations.SerializedName
import com.proyek.infrastructures.inventory.item.entities.Grocery

data class Service (

    @SerializedName("id")
    val id: String?= null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("type")
    val type: Int? = null,

    @SerializedName("agent_fee")
    val agentFee: Int? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("service_fee")
    val serviceFee: Int? = null,

    @SerializedName("groceries")
    val groceries: Grocery? = null
)