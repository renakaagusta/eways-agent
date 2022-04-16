package com.proyek.infrastructures.order.chat.usecases

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyek.infrastructures.order.chat.entities.Chat
import com.proyek.infrastructures.order.chat.network.ChatApiServices
import com.proyek.infrastructures.order.chat.network.ChatResponse
import com.proyek.infrastructures.utils.NetworkErrorHandler
import com.proyek.infrastructures.utils.retrofit.MyRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CreateChat: ViewModel() {
    private val services = ChatApiServices::class.java
    private val result = MutableLiveData<ArrayList<Chat>>()

    fun set(sender: String, receiver: String, content: String, orderId: String, title: String, description: String, context: Context) {
        MyRetrofit
            .createService(services)
            .createChat(sender, receiver, content, orderId, title, description)
            .enqueue(object : Callback<ChatResponse?> {
                override fun onFailure(call: Call<ChatResponse?>?, t: Throwable?) {
                    NetworkErrorHandler.checkFailure(t!!, context)
                }

                override fun onResponse(call: Call<ChatResponse?>?, response: Response<ChatResponse?>?) {
                    if (response!!.isSuccessful) {
                        val results = ArrayList<Chat>()
                        response.body()?.data?.let { results.addAll(it) }
                        result.postValue(results)
                    } else {
                        NetworkErrorHandler.checkResponse(response.code())
                    }
                }
            })
    }
    fun get(): LiveData<java.util.ArrayList<Chat>> {
        return result
    }
}