package com.proyek.infrastructures.user.agent.network

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface AgentApiServices {
    @FormUrlEncoded
    @POST("user/agent/assign")
    fun assignAgent(@Field("agentId") ID: String, @Field("clusterId") clusterId: String): Call<AgentResponse>

    @FormUrlEncoded
    @POST("user/agent/unassign")
    fun unassignAgent(@Field("id") ID: String): Call<AgentResponse>

    @FormUrlEncoded
    @POST("user/agent/create")
    fun createAgent(@Field("userName") username: String, @Field("fullName") fullname: String, @Field("phoneNumber") phoneNumber: String, @Field("email") email: String, @Field("password") password: String,  @Field("address") address: String, @Field("nik") NIK: String, @Field("employeeId") employeeId: String): Call<AgentListResponse>

    @GET("user/agent/data")
    fun getAgentDetail(@Query("id") id: String): Call<AgentListResponse>

    @FormUrlEncoded
    @POST("user/agent/cluster")
    fun getAgentByCluster(@Field("clusterId") clusterId: String): Call<AgentListResponse>

    @GET("user/agent/all")
    fun getAgentList(): Call<AgentListResponse>

    @FormUrlEncoded
    @POST("user/agent/update")
    fun updateAgent(@Field("id") id: String, @Field("userName") username: String, @Field("fullName") fullname: String, @Field("phoneNumber") phoneNumber: String, @Field("email") email: String, @Field("password") password: String,  @Field("address") address: String, @Field("nik") NIK: String, @Field("employeeId") employeeId: String): Call<AgentResponses>

    @DELETE("user/agent/data")
    fun deleteAgent(@Query("id") id: String): Call<AgentResponse>

    @FormUrlEncoded
    @POST("/user/agent/login")
    fun loginAgent(@Field("phoneNumber") phoneNumber: String, @Field("nik") nik: String, @Field("firebaseToken") firebaseToken: String, @Field("otpStatus") otpStatus: Int): Call<AgentResponse>

    @GET("/sendSMS")
    fun sendOTP(
        @Query("phoneNumber") phoneNumber:  String,
        @Query("message") message:  String
    ): Call<String>

    @Multipart
    @POST("/user/agent/upload")
    fun uploadImageProfileAgent(@Query("id") id: String, @Part image: MultipartBody.Part) : Call<AgentListResponse>
}