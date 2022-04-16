package com.proyek.infrastructures.user.customer.usecases

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyek.infrastructures.user.customer.network.CustomerApiServices
import com.proyek.infrastructures.user.customer.network.CustomerResponse
import com.proyek.infrastructures.utils.NetworkErrorHandler
import com.proyek.infrastructures.utils.retrofit.MyRetrofit

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GetCustomerList: ViewModel() {
    private val services = CustomerApiServices::class.java
    private val result = MutableLiveData<CustomerResponse>()

    fun set(context:Context) {

        

        MyRetrofit
            .createService(services)
            .getCustomerList()
            .enqueue(object : Callback<CustomerResponse?> {
                override fun onFailure(call: Call<CustomerResponse?>?, t: Throwable?) {
                    NetworkErrorHandler.checkFailure(t!!, context)
                }

                override fun onResponse(call: Call<CustomerResponse?>?, response: Response<CustomerResponse?>?) {
                    if (response!!.isSuccessful) {
                        val results = response.body()!!
                        result.postValue(results)
                    } else {
                        NetworkErrorHandler.checkResponse(response.code(), context)
                    }
                }
            })
    }
    fun get(): LiveData<CustomerResponse> {
        return result
    }
}