package com.proyek.infrastructures.order.payment.`interface`

interface IPayable {
    fun getPrice(): Int
    fun updateOnPaid()
}