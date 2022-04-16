package com.proyek.infrastructures.order.order.entities

import com.google.gson.annotations.SerializedName
import com.proyek.infrastructures.inventory.item.entities.Item

data class Grocery(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("item")
    val items: List<Item>? = null,

    @SerializedName("quantity")
    val quantity: List<Int>? = null
)