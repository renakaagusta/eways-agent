package com.proyek.infrastructures.user.user.usecases

import android.content.Context
import androidx.lifecycle.ViewModel
import com.proyek.infrastructures.user.user.network.AuthApiServices
import com.proyek.infrastructures.user.user.network.UserResponse
import com.proyek.infrastructures.utils.NetworkErrorHandler
import com.proyek.infrastructures.utils.retrofit.MyRetrofit

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GetUserDetail: ViewModel() {
    private val services = AuthApiServices::class.java
    private var result = ArrayList<UserResponse>()

    fun set(ID:String, context: Context) {
        MyRetrofit
            .createService(services)
            .readUser(ID)
            .enqueue(object : Callback<UserResponse?> {
                override fun onFailure(call: Call<UserResponse?>?, t: Throwable?) {
                    NetworkErrorHandler.checkFailure(t!!, context)
                }

                override fun onResponse(call: Call<UserResponse?>?, response: Response<UserResponse?>?) {
                        if (response!!.isSuccessful) {
                            result = ArrayList()
                            result.add(response.body()!!)
                        } else {
                            NetworkErrorHandler.checkResponse(response.code(), context)
                        }
                }
            })
    }
    fun get(): ArrayList<UserResponse> {
        return result
    }
}