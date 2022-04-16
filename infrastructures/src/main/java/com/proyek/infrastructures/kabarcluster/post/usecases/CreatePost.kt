package com.proyek.infrastructures.kabarcluster.post.usecases

import android.content.Context
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


class CreatePost: ViewModel() {
    private val services = PostApiServices::class.java
    private val result = MutableLiveData<PostResponse>()

    fun set(userId: String, clusterId: String, postContent: String, context: Context) {
        MyRetrofit
            .createService(services)
            .createPost(userId, clusterId, postContent)
            .enqueue(object : Callback<PostResponse?> {
                override fun onFailure(call: Call<PostResponse?>?, t: Throwable?) {
                    NetworkErrorHandler.checkFailure(t!!, context)
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