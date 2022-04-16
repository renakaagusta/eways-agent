package com.eways.agent.order.const

enum class OrderStatus(value : String) {
    Created("Belum dilayani"),
    OnProgress("Sedang dilayani"),
    CustomerFinished("Pelanggan telah menyelesaikan pesanan"),
    AgentFinished("Selesai")
}