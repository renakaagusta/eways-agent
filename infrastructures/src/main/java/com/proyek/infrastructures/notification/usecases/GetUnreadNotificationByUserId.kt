package com.proyek.infrastructures.notification.usecases

import android.content.Context
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.ViewModel
import com.proyek.infrastructures.notification.network.NotificationApiServices
import com.proyek.infrastructures.notification.network.NotificationResponse
import com.proyek.infrastructures.utils.NetworkErrorHandler
import com.proyek.infrastructures.utils.retrofit.MyRetrofit

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GetUnreadNotificationByUserId: ViewModel() {
    private val services = NotificationApiServices::class.java
    private var result = NotificationResponse()

    fun set(context:Context,userId: String) {

        MyRetrofit
            .createService(services)
            .getUnreadNotificationByUserId(userId)
            .enqueue(object : Callback<NotificationResponse?> {
                override fun onFailure(call: Call<NotificationResponse?>?, t: Throwable?) {
                    NetworkErrorHandler.checkFailure(t!!, context)
                }

                override fun onResponse(call: Call<NotificationResponse?>?, response: Response<NotificationResponse?>?) {
                    if (response!!.isSuccessful) {
                        result = response.body()!!
                    } else {
                        NetworkErrorHandler.checkResponse(response.code(), context)
                    }
                }
            })
    }

    fun get(): NotificationResponse {
        return result
    }

}