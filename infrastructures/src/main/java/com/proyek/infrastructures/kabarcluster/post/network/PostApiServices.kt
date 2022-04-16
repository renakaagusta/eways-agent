package com.proyek.infrastructures.kabarcluster.post.network

import retrofit2.Call
import retrofit2.http.*

interface PostApiServices {
    @FormUrlEncoded
    @POST("kabar/post/create")
    fun createPost(@Field("userId") creator: String, @Field("clusterId") clusterId: String, @Field("postContent") postContent: String): Call<PostResponse>

    @GET("kabar/post/data")
    fun getPostDetail(@Query("postId") postId: String): Call<PostResponse>

    @GET("kabar/post/cluster")
    fun getPostByClusterId(@Query("clusterId") clusterId: String): Call<PostResponse>

    @FormUrlEncoded
    @POST("kabar/post/update")
    fun updatePost(@Field("postId") postId: String,  @Field("postContent") postContent: String): Call<PostResponse>

    @FormUrlEncoded
    @POST("kabar/post/delete")
    fun deletePost(@Field("postId") postId: String): Call<PostResponse>

    @FormUrlEncoded
    @POST("kabar/post/pin")
    fun pinPost(@Field("postId") postId: String): Call<PostResponse>

    @FormUrlEncoded
    @POST("kabar/post/unpin")
    fun unpinPost(@Field("postId") postId: String): Call<PostResponse>

}