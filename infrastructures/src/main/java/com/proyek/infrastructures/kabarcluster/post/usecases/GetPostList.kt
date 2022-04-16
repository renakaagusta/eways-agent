package com.proyek.infrastructures.kabarcluster.post.usecases

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyek.infrastructures.kabarcluster.post.network.PostApiServices
import com.proyek.infrastructures.kabarcluster.post.network.PostResponse
import com.proyek.infrastructures.utils.NetworkErrorHandler
import com.proyek.infrastructures.utils.retrofit.MyRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GetPostList: ViewModel() {
    private val services = PostApiServices::class.java
    private val result = MutableLiveData<PostResponse>()

    fun set(clusterId: String, context: Context) {
        MyRetrofit
            .createService(services)
            .getPostByClusterId(clusterId)
            .enqueue(object : Callback<PostResponse?> {
                override fun onFailure(call: Call<PostResponse?>?, t: Throwable?) {
                    NetworkErrorHandler.checkFailure(t!!, context)
                    Log.d("error", t.message)
                }

                override fun onResponse(call: Call<PostResponse?>?, response: Response<PostResponse?>?) {
                    if (response!!.isSuccessful) {
                        val results: PostResponse = response.body()!!
                        result.postValue(results)
                    } else {
                        NetworkErrorHandler.checkResponse(response.code(), context)
                    }
                }
            })
    }
    fun get(): LiveData<PostResponse> {
        return result
    }
}