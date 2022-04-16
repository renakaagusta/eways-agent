package com.proyek.infrastructures.order.payment.`interface`

interface ISender {
    fun pay(amount: Int)
    fun getid(): String
    fun getName(): String
    fun getDescription(): String
}