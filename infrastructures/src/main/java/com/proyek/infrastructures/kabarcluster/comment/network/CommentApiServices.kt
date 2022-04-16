package com.proyek.infrastructures.kabarcluster.comment.network

import retrofit2.Call
import retrofit2.http.*

interface CommentApiServices {
    @FormUrlEncoded
    @POST("kabar/comment/create")
    fun createComment(@Field("userId") userId: String, @Field("postId") postId: String,@Field("commentContent") commentContent: String): Call<CommentResponse>

    @GET("kabar/comment/data")
    fun getComment(@Query("commentId") commentId: String): Call<CommentResponse>


    @GET("kabar/comment/by_post")
    fun getCommentList(@Query("postId") postId: String): Call<CommentResponse>


    @FormUrlEncoded
    @POST("kabar/comment/update")
    fun updateComment(@Field("commentId") commentId: String,@Field("commentContent") commentContent: String): Call<CommentResponse>

    @FormUrlEncoded
    @POST("kabar/comment/delete")
    fun deleteComment(@Field("commentId") postId: String): Call<CommentResponse>
}