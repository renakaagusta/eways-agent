package com.proyek.infrastructures.inventory.internetservice.network

import com.proyek.infrastructures.inventory.internetservice.entities.InternetService

data class InternetServiceResponse (
    var errors: Error = Error(),
    var message: String = "",
    var data: ArrayList<InternetService> = ArrayList<InternetService>()
)