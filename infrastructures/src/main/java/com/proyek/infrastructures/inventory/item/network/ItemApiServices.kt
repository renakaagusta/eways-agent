package com.proyek.infrastructures.inventory.item.network

import com.proyek.infrastructures.inventory.category.entities.Category
import retrofit2.Call
import retrofit2.http.*

interface ItemApiServices {
    @FormUrlEncoded
    @POST("inventory/item/create")
    fun createItem(@Field("name") name: String, @Field("description") description: String, @Field("price") price: Int, @Field("photoPath") photoPath: String, @Field("category") category: Category): Call<ItemResponse>

    @GET("inventory/item/data")
    fun getItemDetail(@Query("itemId") itemId: String): Call<ItemResponse>

    @GET("inventory/item/all")
    fun getItemList(): Call<ItemResponse>

    @FormUrlEncoded
    @POST("inventory/item/update")
    fun updateItem(@Field("name") name: String, @Field("description") description: String, @Field("price") price: Int, @Field("photoPath") photoPath: String, @Field("category") category: Category): Call<ItemResponse>

    @DELETE("inventory/item/data")
    fun deleteItem(@Field("ID") id: String): Call<ItemResponse>

    @GET("inventory/item/category")
    fun getItemByCategory(@Query("categoryId") categoryId: String): Call<ItemResponse>

    @GET("inventory/item/")
    fun setItemCategory(@Field("name") name: String, @Field("description") description: String, @Field("price") price: Int, @Field("photoPath") photoPath: String, @Field("category") category: Category): Call<ItemResponse>

}