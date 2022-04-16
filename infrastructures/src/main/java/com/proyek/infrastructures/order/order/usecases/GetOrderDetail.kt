package com.proyek.infrastructures.order.order.usecases

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.proyek.infrastructures.order.order.entities.Order
import com.proyek.infrastructures.order.order.network.OrderApiServices
import com.proyek.infrastructures.order.order.network.OrderResponse
import com.proyek.infrastructures.utils.NetworkErrorHandler
import com.proyek.infrastructures.utils.retrofit.MyRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GetOrderDetail: ViewModel() {
    private val services = OrderApiServices::class.java
    public val result = ArrayList<Order>()

    fun set(context: Context,ID:String) {

        MyRetrofit
            .createService(services)
            .getOrderDetail(ID)
            .enqueue(object : Callback<OrderResponse?> {
                override fun onFailure(call: Call<OrderResponse?>?, t: Throwable?) {
                    Log.d("errororder", t?.message)
                    NetworkErrorHandler.checkFailure(t!!, context)
                }

                override fun onResponse(call: Call<OrderResponse?>?, response: Response<OrderResponse?>?) {
                    Log.d("orderresponse", response?.body().toString())
                        if (response!!.isSuccessful) {
                            result.addAll(response.body()!!.data)
                        } else {
                            NetworkErrorHandler.checkResponse(response.code(), context)
                        }

                }
            })
    }
    fun get(): java.util.ArrayList<Order> {
        return result
    }
}