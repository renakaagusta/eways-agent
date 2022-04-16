package com.proyek.infrastructures.order.order.`interface`

import com.proyek.infrastructures.order.order.entities.Order

interface IBuyer {
    fun createOrder(orderables: List<Order>): Order
    fun getName(): String
    fun getAddress(): String
    fun getPhoneNumber(): String
    fun getEmail(): String
    fun finishOrder(): String
}