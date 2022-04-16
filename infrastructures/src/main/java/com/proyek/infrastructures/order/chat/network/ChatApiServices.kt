package com.proyek.infrastructures.order.chat.network

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface ChatApiServices {
    @POST("order/chat/create")
    fun createChat(@Query("senderId") sender: String,
                   @Query("receiverId") receiver: String,
                   @Query("chatContent") content: String,
                   @Query("orderId") orderId: String,
                   @Query("notificationTitle") notificationTitle: String,
                   @Query("notificationDescription") notificationDescription: String): Call<ChatResponse>
}