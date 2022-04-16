package com.proyek.infrastructures.inventory.service.network

import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ServiceApiServices {

    @POST("inventory/service/create")
    fun createService(@Query("name") name: String, @Query("description") description: String, @Query("price") price: Int): Call<ServiceResponse>

    @GET("order/service/data")
    fun getServiceDetail(@Query("serviceId") id: String): Call<ServiceResponse>

    @GET("inventory/service/all")
    fun getServiceList(): Call<ServiceResponse>

    @POST("inventory/service/update")
    fun updateService(@Query("name") name: String, @Query("description") description: String, @Query("price") price: Int): Call<ServiceResponse>

    @DELETE("inventory/service/data")
    fun deleteService(@Query("name") name: String): Call<ServiceResponse>

}