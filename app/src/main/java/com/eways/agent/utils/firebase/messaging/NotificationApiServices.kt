package com.eways.agent.utils.firebase.messaging

import retrofit2.Call
import retrofit2.http.*

interface NotificationApiServices {
    @FormUrlEncoded
    @POST("fcm.php")
    fun pushNotification(@Field("title") title: String, @Field("body") body: String, @Field("token") token: String): Call<String>
}