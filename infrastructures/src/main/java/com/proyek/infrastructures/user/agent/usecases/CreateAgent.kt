package com.proyek.infrastructures.user.agent.usecases

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyek.infrastructures.user.agent.entities.Error
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.user.agent.network.AgentApiServices
import com.proyek.infrastructures.user.agent.network.AgentListResponse
import com.proyek.infrastructures.utils.NetworkErrorHandler
import com.proyek.infrastructures.utils.retrofit.MyRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateAgent: ViewModel() {
    private val services = AgentApiServices::class.java
    private val result = MutableLiveData<AgentListResponse>()

    fun set(username: String, fullname: String, phoneNumber: String, email: String, password: String, address: String, NIK: String, context: Context, employeeId: String) {

        MyRetrofit
            .createService(services)
            .createAgent(username, fullname, phoneNumber, email, password, address, NIK, employeeId)
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
                    NetworkErrorHandler.checkFailure(t, context)
                }

                override fun onResponse(call: Call<AgentListResponse?>?, response: Response<AgentListResponse?>?) {
                    if (response!!.isSuccessful) {
                        val results: AgentListResponse = response.body()!!
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