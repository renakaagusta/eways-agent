package com.proyek.infrastructures.user.cluster.usecases

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyek.infrastructures.user.cluster.network.ClusterApiServices
import com.proyek.infrastructures.user.cluster.network.ClusterResponse
import com.proyek.infrastructures.utils.retrofit.MyRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GetClusterDetail: ViewModel() {
    private val services = ClusterApiServices::class.java
    private val result = MutableLiveData<ClusterResponse>()

    fun set(id:String) {
        MyRetrofit
            .createService(services)
            .getClusterDetail(id)
            .enqueue(object : Callback<ClusterResponse?> {
                override fun onFailure(call: Call<ClusterResponse?>?, t: Throwable?) {
                    Log.d("response", t?.message)
                }

                override fun onResponse(call: Call<ClusterResponse?>?, response: Response<ClusterResponse?>?) {
                    if (response!!.isSuccessful) {
                        val results: ClusterResponse = response.body()!!
                        result.postValue(results)
                    } else {
                    }
                }
            })
    }
    fun get(): LiveData<ClusterResponse> {
        return result
    }
}