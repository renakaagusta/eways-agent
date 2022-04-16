package com.proyek.infrastructures.order.order.usecases

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyek.infrastructures.order.order.entities.Order
import com.proyek.infrastructures.order.order.network.OrderApiServices
import com.proyek.infrastructures.order.order.network.OrderResponse
import com.proyek.infrastructures.utils.NetworkErrorHandler
import com.proyek.infrastructures.utils.retrofit.MyRetrofit

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DeclineOrder: ViewModel() {
    private val services = OrderApiServices::class.java
    private val result = MutableLiveData<ArrayList<Order>>()

    internal fun set(id: String, context: Context) {

        

        MyRetrofit
            .createService(services)
            .declineOrder(id)
            .enqueue(object : Callback<OrderResponse?> {
                override fun onFailure(call: Call<OrderResponse?>?, t: Throwable?) {
                    NetworkErrorHandler.checkFailure(t!!, context)
                }

                override fun onResponse(call: Call<OrderResponse?>?, response: Response<OrderResponse?>?) {
                    if (response!!.isSuccessful) {
                        val results = ArrayList<Order>()
                        response.body()?.data?.let { results.addAll(it) }
                        result.postValue(results)
                    } else {
                        NetworkErrorHandler.checkResponse(response.code(), context)
                    }
                }
            })
    }
    internal fun get(): LiveData<java.util.ArrayList<Order>> {
        return result
    }
}