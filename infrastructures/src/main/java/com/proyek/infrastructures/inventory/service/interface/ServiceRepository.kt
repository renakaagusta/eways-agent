package com.proyek.infrastructures.inventory.service.`interface`

import android.app.Service
import com.proyek.infrastructures.inventory.service.network.ServiceResponse

interface ServiceRepository {
    fun all(): ServiceResponse
    fun find(): Service
    fun save()
}