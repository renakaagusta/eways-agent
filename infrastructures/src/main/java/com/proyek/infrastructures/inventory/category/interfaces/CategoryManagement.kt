package com.proyek.infrastructures.inventory.category.interfaces

import com.proyek.infrastructures.inventory.category.entities.Category
import com.proyek.infrastructures.inventory.category.network.CategoryResponse

interface CategoryManagement {
    fun all(): CategoryResponse
    fun find(): Category
    fun save()
}