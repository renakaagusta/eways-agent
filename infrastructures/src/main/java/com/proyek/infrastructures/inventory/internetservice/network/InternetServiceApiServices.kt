package com.proyek.infrastructures.inventory.internetservice.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface InternetServiceApiServices {

    @GET("inventory/internet/data")
    fun getServiceDetail(@Query("serviceId") id: String): Call<InternetServiceResponse>

    @GET("inventory/internet/all")
    fun getServiceList(): Call<InternetServiceResponse>

}