package com.proyek.infrastructures.notification.usecases

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyek.infrastructures.notification.network.NotificationApiServices
import com.proyek.infrastructures.notification.network.NotificationResponse
import com.proyek.infrastructures.utils.NetworkErrorHandler
import com.proyek.infrastructures.utils.retrofit.MyRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CreateChatNotification: ViewModel() {
    private val services = NotificationApiServices::class.java
    private val result = MutableLiveData<ArrayList<NotificationResponse>>()

    fun set(receiver: String, chatId: String,title: String, description: String, context: Context) {
        MyRetrofit
            .createService(services)
            .createChatNotification(receiver, chatId,title, description)
            .enqueue(object : Callback<NotificationResponse?> {
                override fun onFailure(call: Call<NotificationResponse?>?, t: Throwable?) {
                    NetworkErrorHandler.checkFailure(t!!, context)
                }

                override fun onResponse(call: Call<NotificationResponse?>?, response: Response<NotificationResponse?>?) {
                    if (response!!.isSuccessful) {
                        val results = ArrayList<NotificationResponse>()
                        response.body()?.let { results.add(it) }
                        result.postValue(results)
                    } else {
                        NetworkErrorHandler.checkResponse(response.code(), context)
                    }
                }
            })
    }

    fun get(): LiveData<java.util.ArrayList<NotificationResponse>> {
        return result
    }

}