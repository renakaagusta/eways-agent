package com.proyek.infrastructures.order.order.`interface`

import com.proyek.infrastructures.order.order.entities.Order

interface IOrderRepository {
    fun findById(): Order
    fun findByBuyer(): List<Order>
    fun findByCourier(): List<Order>
    fun save()
    fun findUnpaidOrderByBuyer(): List<Order>
    fun findPaidOrderByBuyer(): List<Order>
    fun findOngoingByUser(): List<Order>
}