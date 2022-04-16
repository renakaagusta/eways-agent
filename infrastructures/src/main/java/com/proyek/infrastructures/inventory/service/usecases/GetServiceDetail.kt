package com.proyek.infrastructures.inventory.service.usecases

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyek.infrastructures.inventory.service.entities.Service
import com.proyek.infrastructures.inventory.service.network.ServiceApiServices
import com.proyek.infrastructures.inventory.service.network.ServiceResponse
import com.proyek.infrastructures.utils.NetworkErrorHandler
import com.proyek.infrastructures.utils.retrofit.MyRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GetServiceDetail: ViewModel() {
    private val services = ServiceApiServices::class.java
    private val result = MutableLiveData<ArrayList<Service>>()

    fun set(serviceId:String) {
        MyRetrofit
            .createService(services)
            .getServiceDetail(serviceId)
            .enqueue(object : Callback<ServiceResponse?> {
                override fun onFailure(call: Call<ServiceResponse?>?, t: Throwable?) {
                    Log.d("errorservice", t?.message)
                }

                override fun onResponse(call: Call<ServiceResponse?>?, response: Response<ServiceResponse?>?) {
                    if (response!!.isSuccessful) {
                        val results: ArrayList<Service> = ArrayList()
                        response.body()!!.data?.let { results.addAll(it) }
                        result.postValue(results)
                    } else {
                        NetworkErrorHandler.checkResponse(response.code())
                    }
                }
            })
    }
    fun get(): LiveData<java.util.ArrayList<Service>> {
        return result
    }
}