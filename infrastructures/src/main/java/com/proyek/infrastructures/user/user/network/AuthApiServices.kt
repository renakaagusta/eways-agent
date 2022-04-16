package com.proyek.infrastructures.user.user.network

import retrofit2.Call
import retrofit2.http.*

interface AuthApiServices {
    @FormUrlEncoded
    @POST("user/admin/active")
    fun activeUser(@Field("id") ID: String): Call<UserResponse>

    @FormUrlEncoded
    @POST("user/admin/deactive")
    fun deactiveUser(@Field("id") ID: String): Call<UserResponse>

    @FormUrlEncoded
    @POST("user/admin/verif")
    fun verifUser(@Field("id") ID: String): Call<UserResponse>

    @FormUrlEncoded
    @POST("user/admin/unverif")
    fun unverifUser(@Field("id") ID: String): Call<UserResponse>

    @FormUrlEncoded
    @POST("user/admin/update")
    fun updateUser(@Field("id") id:String, @Field("userName") username: String, @Field("fullName") fullname: String, @Field("phoneNumber") phoneNumber: String, @Field("email") email: String, @Field("password") password: String, @Field("address") address: String): Call<UserResponse>

    @DELETE("user/admin/data")
    fun deleteUser(@Query("id") id: String): Call<UserResponse>

    @FormUrlEncoded
    @POST("user/admin/login")
    fun Login(@Field("userName") username: String, @Field("password") password: String): Call<UserResponse>

    @FormUrlEncoded
    @POST("user/admin/create")
    fun createUser(@Field("userName") username: String, @Field("fullName") fullname: String, @Field("phoneNumber") phoneNumber: String, @Field("email") email: String, @Field("password") password: String, @Field("address") address: String): Call<UserResponse>

    @GET("user/admin/data")
    fun readUser(@Query("id") id: String): Call<UserResponse>

}