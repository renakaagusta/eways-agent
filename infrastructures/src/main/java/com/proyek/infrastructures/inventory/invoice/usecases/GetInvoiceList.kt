package com.proyek.infrastructures.inventory.invoice.usecases

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyek.infrastructures.inventory.invoice.network.InvoiceApiServices
import com.proyek.infrastructures.inventory.invoice.network.InvoiceResponse
import com.proyek.infrastructures.utils.NetworkErrorHandler
import com.proyek.infrastructures.utils.retrofit.MyRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GetInvoiceList: ViewModel() {
    private val services = InvoiceApiServices::class.java
    private val result = MutableLiveData<InvoiceResponse>()

    fun set(context: Context) {
        MyRetrofit
            .createService(services)
            .getInvoiceList()
            .enqueue(object : Callback<InvoiceResponse?> {
                override fun onFailure(call: Call<InvoiceResponse?>?, t: Throwable?) {
                    NetworkErrorHandler.checkFailure(t!!, context)
                }

                override fun onResponse(call: Call<InvoiceResponse?>?, response: Response<InvoiceResponse?>?) {
                    if (response!!.isSuccessful) {
                        val results: InvoiceResponse = response.body()!!

                        result.postValue(results)
                    } else {
                        NetworkErrorHandler.checkResponse(response.code())
                    }
                }
            })
    }
    fun get(): LiveData<InvoiceResponse> {
        return result
    }
}