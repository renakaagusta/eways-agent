package com.eways.agent.order.const

enum class OrderType(val value: String) {
    PSB("Pesan Pasang Baru"),
    PickupTicket("Laporan Kerusakan"),
    GantiPaket("Pesan Ganti Paket"),
    TitipPaket("Titip Paket"),
    SOPP("SOPP"),
    TitipBelanja("Titip Belanja"),
    LayananBebas("Layanan Bebas")
}