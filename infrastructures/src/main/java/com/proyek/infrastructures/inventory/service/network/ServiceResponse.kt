package com.proyek.infrastructures.inventory.service.network

import com.proyek.infrastructures.inventory.service.entities.Service


data class ServiceResponse (
    val errors: Error,
    val message: String,
    val data: List<Service>
)