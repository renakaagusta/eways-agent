package com.proyek.infrastructures.user.agent.usecases

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyek.infrastructures.user.agent.entities.UserAgent

import com.proyek.infrastructures.user.agent.network.AgentApiServices
import com.proyek.infrastructures.user.agent.network.AgentDataResponse
import com.proyek.infrastructures.user.agent.network.AgentResponse
import com.proyek.infrastructures.utils.NetworkErrorHandler
import com.proyek.infrastructures.utils.retrofit.MyRetrofit
import com.proyek.infrastructures.user.agent.entities.Error
import com.proyek.infrastructures.user.agent.network.AgentListResponse

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadImageProfileAgent: ViewModel() {
    private val services = AgentApiServices::class.java
    private val result = MutableLiveData<AgentListResponse>()

    fun set(id: String, img: MultipartBody.Part, context: Context) {

        MyRetrofit
            .createService(services)
            .uploadImageProfileAgent(id, img)
            .enqueue(object : Callback<AgentListResponse?> {
                override fun onFailure(call: Call<AgentListResponse?>?, t: Throwable?) {
                    val messages = ArrayList<String>()
                    messages.add(t?.message!!)
                    result.postValue(AgentListResponse(
                        Error(messages),
                        "",
                        ArrayList()
                    ))
                    NetworkErrorHandler.checkFailure(t, context)
                }

                override fun onResponse(call: Call<AgentListResponse?>?, response: Response<AgentListResponse?>?) {

                    if (response!!.isSuccessful) {
                        val results = response.body()!!
                        result.postValue(results)
                    } else {
                        NetworkErrorHandler.checkResponse(response.code(), context)
                    }
                }
            })
    }
    fun get(): LiveData<AgentListResponse> {
        return result
    }
}