package com.proyek.infrastructures.user.agent.usecases

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.proyek.infrastructures.order.order.entities.Order
import com.proyek.infrastructures.user.agent.entities.Agent
import com.proyek.infrastructures.user.agent.entities.Error
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.user.agent.network.AgentApiServices
import com.proyek.infrastructures.user.agent.network.AgentDataResponse
import com.proyek.infrastructures.user.agent.network.AgentListResponse
import com.proyek.infrastructures.user.agent.network.AgentResponse

import com.proyek.infrastructures.utils.NetworkErrorHandler
import com.proyek.infrastructures.utils.retrofit.MyRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetAgentDetail: ViewModel() {
    private val services = AgentApiServices::class.java
    private val result = MutableLiveData<AgentListResponse>()

    fun set(id: String, context: Context) {

        MyRetrofit
            .createService(services)
            .getAgentDetail(id)
            .enqueue(object : Callback<AgentListResponse?> {
                override fun onFailure(call: Call<AgentListResponse?>?, t: Throwable?) {
                    val messages = ArrayList<String>()
                    messages.add(t?.message!!)
                    result.postValue(
                        AgentListResponse(
                            Error(messages),
                            "",
                            ArrayList<UserAgent>()
                        )
                    )
                    Log.d("result", result.value.toString())
                    Log.d("agenterror", t?.message)
                }

                override fun onResponse(call: Call<AgentListResponse?>?, response: Response<AgentListResponse?>?) {
                    if (response!!.isSuccessful) {
                        val results = response.body()
                        Log.w("GetAgentDetail", response.body()!!.data.toString())
                        result.postValue(results)
                    } else {

                    }
                    Log.d("agenterror", response.body().toString())

                }
            })
    }
    fun get(): LiveData<AgentListResponse> {
        return result
    }
}