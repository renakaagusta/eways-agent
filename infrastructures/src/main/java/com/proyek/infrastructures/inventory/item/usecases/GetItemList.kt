package com.proyek.infrastructures.inventory.item.usecases

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyek.infrastructures.inventory.item.network.ItemApiServices
import com.proyek.infrastructures.inventory.item.network.ItemResponse
import com.proyek.infrastructures.utils.NetworkErrorHandler
import com.proyek.infrastructures.utils.retrofit.MyRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GetItemList: ViewModel() {
    private val services = ItemApiServices::class.java
    private val result = MutableLiveData<ItemResponse>()

    internal fun set(context: Context) {
        MyRetrofit
            .createService(services)
            .getItemList()
            .enqueue(object : Callback<ItemResponse?> {
                override fun onFailure(call: Call<ItemResponse?>?, t: Throwable?) {
                    NetworkErrorHandler.checkFailure(t!!, context)
                }

                override fun onResponse(call: Call<ItemResponse?>?, response: Response<ItemResponse?>?) {
                    if (response!!.isSuccessful) {
                        val results: ItemResponse = response.body()!!

                        result.postValue(results)
                    } else {
                        NetworkErrorHandler.checkResponse(response.code())
                    }
                }
            })
    }
    internal fun get(): LiveData<ItemResponse> {
        return result
    }
}