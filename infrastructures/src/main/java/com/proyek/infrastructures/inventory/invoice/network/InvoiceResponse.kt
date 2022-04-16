package com.proyek.infrastructures.inventory.invoice.network

import com.proyek.infrastructures.inventory.invoice.entities.Invoice

data class InvoiceResponse (
    val errors: Error,
    val message: String,
    val data : List<Invoice>
)