package com.proyek.infrastructures.user.agent.usecases

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import com.proyek.infrastructures.user.agent.network.AgentApiServices
import com.proyek.infrastructures.user.customer.network.CustomerApiServices
import com.proyek.infrastructures.utils.NetworkErrorHandler
import com.proyek.infrastructures.utils.retrofit.MyRetrofit
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SendOTP : ViewModel() {
    private val services = AgentApiServices::class.java
    private val result = MutableLiveData<String>()
    var gson = GsonBuilder()
    .setLenient()
    .create()
    val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl("http://167.99.70.77:4000/")
        .build()


    fun set(
        phoneNumber: String,
        message: String
    ) {
        retrofit.create(services)
            .sendOTP(phoneNumber, message)
            .enqueue(object : Callback<String?> {
                override fun onFailure(call: Call<String?>?, t: Throwable?) {
                    Log.d("error", call.toString())
                    Log.d("throw", t?.message)

                }

                override fun onResponse(
                    call: Call<String?>?,
                    response: Response<String?>?
                ) {
                    if (response!!.isSuccessful) {
                        val results = response.body()!!
                        result.postValue(results)
                    } else {
                        NetworkErrorHandler.checkResponse(response.code())
                    }
                }
            })
    }

    fun get(): LiveData<String> {
        return result
    }
}