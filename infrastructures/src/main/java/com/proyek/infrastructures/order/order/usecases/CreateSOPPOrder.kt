package com.proyek.infrastructures.order.order.usecases

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyek.infrastructures.order.order.network.OrderApiServices
import com.proyek.infrastructures.order.order.network.OrderResponse
import com.proyek.infrastructures.order.order.network.body.SOPPBody
import com.proyek.infrastructures.utils.NetworkErrorHandler
import com.proyek.infrastructures.utils.retrofit.MyRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CreateSOPPOrder: ViewModel() {
    private val services = OrderApiServices::class.java
    private val result = MutableLiveData<OrderResponse>()

    fun set(soppBody: SOPPBody, context: Context) {
        MyRetrofit
            .createService(services)
            .createSOPPOrder(soppBody)
            .enqueue(object : Callback<OrderResponse?> {
                override fun onFailure(call: Call<OrderResponse?>?, t: Throwable?) {
                    Log.d("error", t?.message)
                    NetworkErrorHandler.checkFailure(t!!, context)
                }

                override fun onResponse(call: Call<OrderResponse?>?, response: Response<OrderResponse?>?) {
                    if (response!!.isSuccessful) {
                        result.postValue(response.body())
                    } else {
                        NetworkErrorHandler.checkResponse(response.code())
                    }
                }
            })
    }
    fun get(): LiveData<OrderResponse> {
        return result
    }
}