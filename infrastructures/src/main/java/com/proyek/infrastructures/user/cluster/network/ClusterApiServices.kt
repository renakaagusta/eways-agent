package com.proyek.infrastructures.user.cluster.network

import retrofit2.Call
import retrofit2.http.*

interface ClusterApiServices {

    @FormUrlEncoded
    @POST("user/cluster/create")
    fun createCluster(@Field("name") name: String, @Field("areas[]") areas: ArrayList<String>): Call<ClusterResponse>

    @FormUrlEncoded
    @POST("user/cluster/update")
    fun updateCluster(@Field("id") id: String,@Field("name") name: String,@Field("areas[]") areas: ArrayList<String>): Call<ClusterResponse>

    @DELETE("user/cluster/data")
    fun deleteCluster(@Query("id") id: String): Call<ClusterResponse>

    @GET("user/cluster/data")
    fun getClusterDetail(@Query("id") id: String): Call<ClusterResponse>

    @GET("user/cluster/all")
    fun getClusterList(): Call<ClusterResponse>

}