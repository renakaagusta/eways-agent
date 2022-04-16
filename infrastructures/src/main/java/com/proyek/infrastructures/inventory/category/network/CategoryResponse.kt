package com.proyek.infrastructures.inventory.category.network

import com.proyek.infrastructures.inventory.category.entities.Category

data class CategoryResponse (
   val errors: Error,
   val message: String,
   val data: List<Category>
)