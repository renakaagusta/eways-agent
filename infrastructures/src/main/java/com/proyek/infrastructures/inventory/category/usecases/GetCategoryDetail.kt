package com.proyek.infrastructures.inventory.category.usecases

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyek.infrastructures.inventory.category.network.CategoryApiServices
import com.proyek.infrastructures.inventory.category.network.CategoryResponse
import com.proyek.infrastructures.utils.NetworkErrorHandler
import com.proyek.infrastructures.utils.retrofit.MyRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GetCategoryDetail: ViewModel() {
    private val services = CategoryApiServices::class.java
    private val result = MutableLiveData<CategoryResponse>()

    internal fun set(categoryId:String, context: Context) {
        MyRetrofit
            .createService(services)
            .getCategoryDetail(categoryId)
            .enqueue(object : Callback<CategoryResponse?> {
                override fun onFailure(call: Call<CategoryResponse?>?, t: Throwable?) {
                    NetworkErrorHandler.checkFailure(t!!, context)
                }

                override fun onResponse(call: Call<CategoryResponse?>?, response: Response<CategoryResponse?>?) {
                    if (response!!.isSuccessful) {
                        val results: CategoryResponse = response.body()!!
                        result.postValue(results)
                    } else {
                        NetworkErrorHandler.checkResponse(response.code())
                    }
                }
            })
    }
    internal fun get(): LiveData<CategoryResponse> {
        return result
    }
}