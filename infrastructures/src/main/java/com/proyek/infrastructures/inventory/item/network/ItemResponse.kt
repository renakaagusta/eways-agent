package com.proyek.infrastructures.inventory.item.network

import com.proyek.infrastructures.inventory.item.entities.Item

data class ItemResponse (
    val errors: Error,
    val message: String,
    val data : List<Item>
)