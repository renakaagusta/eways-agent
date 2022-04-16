package com.proyek.infrastructures.user.agent.usecases

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyek.infrastructures.user.agent.entities.Error
import com.proyek.infrastructures.user.agent.network.AgentApiServices
import com.proyek.infrastructures.user.agent.network.AgentDataResponse
import com.proyek.infrastructures.user.agent.network.AgentResponses

import com.proyek.infrastructures.utils.NetworkErrorHandler
import com.proyek.infrastructures.utils.retrofit.MyRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class UpdateAgent: ViewModel() {
    private val services = AgentApiServices::class.java
    private val result = MutableLiveData<AgentResponses>()

    fun set(id: String, username: String, fullname: String, phoneNumber: String, email: String, password: String, address: String, NIK: String, context: Context, employeeId: String) {

        MyRetrofit
            .createService(services)
            .updateAgent(id, username, fullname, phoneNumber, email, password, address,  NIK, employeeId)
            .enqueue(object : Callback<AgentResponses?> {
                override fun onFailure(call: Call<AgentResponses?>?, t: Throwable?) {
                    val messages = ArrayList<String>()
                    messages.add(t?.message!!)
                    result.postValue(
                        AgentResponses(
                            Error(messages),
                            "",
                            ArrayList<AgentDataResponse>()
                        )
                    )

                    NetworkErrorHandler.checkFailure(t, context)
                }

                override fun onResponse(call: Call<AgentResponses?>?, response: Response<AgentResponses?>?) {
                        if (response!!.isSuccessful) {
                            val results: AgentResponses = response.body()!!
                            result.postValue(results)
                        } else {
                            NetworkErrorHandler.checkResponse(response.code(), context)
                        }
                }
            })
    }
    fun get(): LiveData<AgentResponses> {
        return result
    }
}