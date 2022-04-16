package com.proyek.infrastructures.notification.network

import retrofit2.Call
import retrofit2.http.*

interface NotificationApiServices {
    @FormUrlEncoded
    @POST("notification/create_chat")
    fun createChatNotification(@Field("receiverId") receiver: String?, @Field("chatId") chatId: String?, @Field("title") title: String?, @Field("description") description: String?): Call<NotificationResponse>

    @FormUrlEncoded
    @POST("notification/create_order")
    fun createOrderNotification(@Field("receiverId") receiver: String?, @Field("orderId") orderId: String, @Field("title") title: String?, @Field("description") description: String?): Call<NotificationResponse>

    @FormUrlEncoded
    @POST("notification/read")
    fun readNotification(@Field("typeId[]") typeId: ArrayList<String>): Call<NotificationResponse>

    @GET("notification/unread")
    fun getUnreadNotificationByUserId(@Query("userId") id: String?): Call<NotificationResponse>

    @GET("notification/data")
    fun getNotificationByUserId(@Query("userId") id: String?): Call<NotificationResponse>
}