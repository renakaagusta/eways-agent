package com.proyek.infrastructures.order.order.`interface`

import com.proyek.infrastructures.order.order.entities.Order

interface ICourier {
    fun takeOrder(order: Order)
    fun getName() : String
    fun getAddress() : String
    fun getPhoneNumber() : String
    fun getEmail() : String
    fun deliverOrder()
}