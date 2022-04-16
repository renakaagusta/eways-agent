package com.proyek.infrastructures.user.customer.usecases

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyek.infrastructures.user.customer.network.CustomerApiServices
import com.proyek.infrastructures.user.customer.network.CustomerResponses
import com.proyek.infrastructures.utils.NetworkErrorHandler
import com.proyek.infrastructures.utils.retrofit.MyRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetCustomerDetail: ViewModel() {
    private val services = CustomerApiServices::class.java
    private val result = MutableLiveData<CustomerResponses>()
    

    fun set(customerId:String, context: Context) {
        MyRetrofit
            .createService(services)
            .getCustomerDetail(customerId)
            .enqueue(object : Callback<CustomerResponses?> {
                override fun onFailure(call: Call<CustomerResponses?>?, t: Throwable?) {
                    NetworkErrorHandler.checkFailure(t!!, context)
                }

                override fun onResponse(call: Call<CustomerResponses?>?, response: Response<CustomerResponses?>?) {

                    if (response!!.isSuccessful) {
                        val results = response.body()!!
                        result.postValue(results)
                    } else {
                        NetworkErrorHandler.checkResponse(response.code(), context)
                    }
                }
            })
    }
    fun get(): LiveData<CustomerResponses> {
        return result
    }
}