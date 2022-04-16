package com.proyek.infrastructures.order.order.network

import com.proyek.infrastructures.order.order.entities.Order
import com.proyek.infrastructures.user.agent.entities.Error

data class OrderResponse (
    val errors: Error,
    val message: String,
    val data: List<Order>
)