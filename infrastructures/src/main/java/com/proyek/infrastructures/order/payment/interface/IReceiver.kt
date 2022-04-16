package com.proyek.infrastructures.order.payment.`interface`

interface IReceiver {
    fun earn(amount: Int): Int
    fun getid(): String
    fun getName(): String
    fun getDescription(): String
}