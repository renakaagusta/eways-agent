package com.proyek.infrastructures.notification.usecases

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyek.infrastructures.notification.network.NotificationApiServices
import com.proyek.infrastructures.notification.network.NotificationResponse
import com.proyek.infrastructures.utils.retrofit.MyRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReadNotification: ViewModel() {
    private val services = NotificationApiServices::class.java
    val result = MutableLiveData<ArrayList<NotificationResponse>>()

    fun set(ids: ArrayList<String>) {

        MyRetrofit
            .createService(services)
            .readNotification(ids)
            .enqueue(object : Callback<NotificationResponse?> {
                override fun onFailure(call: Call<NotificationResponse?>?, t: Throwable?) {
                }

                override fun onResponse(call: Call<NotificationResponse?>?, response: Response<NotificationResponse?>?) {
                    if (response!!.isSuccessful) {
                        val results = ArrayList<NotificationResponse>()
                        response.body()?.let { results.add(it) }
                        result.postValue(results)
                    }
                }
            })
    }

    fun get(): LiveData<java.util.ArrayList<NotificationResponse>> {
        return result
    }
}