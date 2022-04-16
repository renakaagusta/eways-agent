package com.proyek.infrastructures.user.agent.usecases

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyek.infrastructures.user.agent.network.AgentApiServices
import com.proyek.infrastructures.user.agent.network.AgentResponse
import com.proyek.infrastructures.utils.BaseActivity
import com.proyek.infrastructures.user.agent.entities.Error
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.user.agent.network.AgentDataResponse
import com.proyek.infrastructures.utils.NetworkErrorHandler
import com.proyek.infrastructures.utils.SweetAlert
import com.proyek.infrastructures.utils.retrofit.MyRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginAgent: ViewModel() {
    private val services = AgentApiServices::class.java
    private val result = MutableLiveData<AgentResponse>()
    private val alert = SweetAlert()

    fun set(phoneNumber: String, nik: String, firebaseToken: String, otpStatus: Int, context: Context) {
        MyRetrofit
            .createService(services)
            .loginAgent("+62"+phoneNumber, nik, firebaseToken, otpStatus)
            .enqueue(object : Callback<AgentResponse?> {
                override fun onFailure(call: Call<AgentResponse?>?, t: Throwable?) {
                    val message = ArrayList<String>()
                    message.add(t?.message!!)
                    result.postValue(AgentResponse(
                        Error(message),"", AgentDataResponse(UserAgent(), "")
                    ))
                    NetworkErrorHandler.checkFailure(t!!, context)
                }

                override fun onResponse(call: Call<AgentResponse?>?, response: Response<AgentResponse?>?) {

                    if (response!!.isSuccessful) {
                        val results = response.body()
                        result.postValue(results)
                    } else {
                        NetworkErrorHandler.checkResponse(response.code(), context)
                    }
                }
            })
    }
    fun get(): LiveData<AgentResponse> {
        return result
    }
}