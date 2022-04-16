package com.proyek.infrastructures.inventory.category.network

import retrofit2.Call
import retrofit2.http.*

interface CategoryApiServices {

    @FormUrlEncoded
    @POST("inventory/category/create")
    fun createCategory(@Field("name") name: String, @Field("description") description: String): Call<CategoryResponse>

    @GET("inventory/category/data")
    fun getCategoryDetail(@Query("id") id: String): Call<CategoryResponse>

    @GET("inventory/category/all")
    fun getCategoryList(): Call<CategoryResponse>

    @FormUrlEncoded
    @POST("inventory/category/update")
    fun updateCategory(@Field("name") name: String, @Field("description") description: String): Call<CategoryResponse>

    @DELETE("inventory/category/data")
    fun deleteCategory(@Field("id") id: String): Call<CategoryResponse>
}