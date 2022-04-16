package com.proyek.infrastructures.user.customer.network

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface CustomerApiServices {
    @FormUrlEncoded
    @POST("user/customer/create")
    fun createCustomer(@Field("userName") username: String, @Field("fullName") fullname: String, @Field("phoneNumber") phoneNumber: String, @Field("email") email: String, @Field("password") password: String,  @Field("address") address: String, @Field("latitude") latitude: Float, @Field("longitude") longitude: Float): Call<CustomerResponse>

    @GET("user/customer/verified")
    fun getVerifiedCustomerList(): Call<CustomerResponse>

    @GET("user/customer/unverified")
    fun getUnverifiedCustomerList(): Call<CustomerResponse>

    @FormUrlEncoded
    @POST("user/customer/update")
    fun updateCustomer(@Field("id") id: String,@Field("userName") username: String, @Field("fullName") fullname: String, @Field("phoneNumber") phoneNumber: String, @Field("email") email: String, @Field("password") password: String,  @Field("address") address: String, @Field("latitude") latitude: Float, @Field("longitude") longitude: Float): Call<CustomerResponse>

    @DELETE("user/customer/data")
    fun deleteCustomer(@Query("id") id: String): Call<CustomerResponse>

    @FormUrlEncoded
    @POST("user/customer/verify")
    fun acceptCustomerVerification(@Field("id") id: String): Call<CustomerResponse>

    @FormUrlEncoded
    @POST("user/customer/unverify")
    fun declineCustomerVerification(@Field("id") id: String): Call<CustomerResponse>

    @FormUrlEncoded
    @POST("user/customer/assign")
    fun assignCustomer(@Field("customerId") customerId: String, @Field("clusterId") clusterId: String): Call<CustomerResponse>

    @FormUrlEncoded
    @POST("user/customer/unasssign")
    fun unassignCustomer(@Field("id") id: String): Call<CustomerResponse>

    @FormUrlEncoded
    @POST("user/customer/login")
    fun loginCustomer(@Field("phoneNumber") phoneNumber: String, @Field("firebaseToken") firebaseToken: String, @Field("otpStatus") otpStatus: Int): Call<CustomerResponse>

    @GET("user/customer/data")
    fun getCustomerDetail(@Query("id") id: String): Call<CustomerResponses>

    @GET("user/customer/all")
    fun getCustomerList(): Call<CustomerResponse>

    @Multipart
    @POST("/user/customer/upload")
    fun uploadImageProfileCustomer(@Query("id") id: String, @Part image: MultipartBody.Part) : Call<CustomerResponse>
}