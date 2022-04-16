package com.eways.agent.notification.const

enum class NotificationType(val value: String){
    Created("Anda mendapat pesanan baru"),
    OnProgress("Order masih diproses"),
    Finished("Customer sudah menyelesaikan order"),
    Billing("Tagihan Pelanggan")
}