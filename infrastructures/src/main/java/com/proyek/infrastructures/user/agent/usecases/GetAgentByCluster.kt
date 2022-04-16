package com.proyek.infrastructures.user.agent.usecases

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyek.infrastructures.order.order.entities.Order
import com.proyek.infrastructures.user.agent.entities.Agent
import com.proyek.infrastructures.user.agent.entities.Error
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.user.agent.network.AgentApiServices
import com.proyek.infrastructures.user.agent.network.AgentListResponse
import com.proyek.infrastructures.utils.retrofit.MyRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetAgentByCluster: ViewModel() {
    private val services = AgentApiServices::class.java
    private val result = MutableLiveData<AgentListResponse>()

    fun set(id: String, context: Context) {

        MyRetrofit
            .createService(services)
            .getAgentByCluster(id)
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
                }

                override fun onResponse(call: Call<AgentListResponse?>?, response: Response<AgentListResponse?>?) {
                    if (response!!.isSuccessful) {
                        val results = response.body()
                        result.postValue(results)
                    } else {

                    }
                }
            })
    }
    fun get(): LiveData<AgentListResponse> {
        return result
    }
}