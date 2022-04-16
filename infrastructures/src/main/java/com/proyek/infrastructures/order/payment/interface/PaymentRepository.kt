package com.proyek.infrastructures.order.payment.`interface`

import com.proyek.infrastructures.order.payment.entities.Payment

interface PaymentRepository {
    fun find(): Payment
    fun save()
}