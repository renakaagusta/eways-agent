package com.proyek.infrastructures.inventory.item.interfaces

import com.proyek.infrastructures.inventory.item.entities.Item
import com.proyek.infrastructures.inventory.item.network.ItemResponse

interface ItemRepository {
    fun all(): ItemResponse
    fun find(): Item
    fun findByCategory(): ItemResponse
    fun save()
}