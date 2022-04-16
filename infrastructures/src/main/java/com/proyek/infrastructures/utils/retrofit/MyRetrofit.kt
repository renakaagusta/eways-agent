package com.proyek.infrastructures.utils.retrofit

import android.content.Context
import android.util.Log
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.proyek.infrastructures.BuildConfig
import com.proyek.infrastructures.utils.Authenticated
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object MyRetrofit {
    private const val BASE_URL = "http://54.169.249.196:8001/"
    private const val BASE_LOCAL_URL = "http://pabi.rumahsinergikarya.com/"
    var context: Context ?= null

    private val client: OkHttpClient = buildClient()!!
    private val retrofit = iniRetrofit(client)
    private val retrofitLocal: Retrofit = iniRetrofitLocal(client)

    private fun buildClient(): OkHttpClient? {
        val builder = OkHttpClient.Builder().addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                var request = chain.request()
                val builder = request.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Connection", "close")

                if (Authenticated.getAccessToken() != null) {
                    builder.addHeader(
                        "Authorization",
                        "Bearer " + Authenticated.getAccessToken()
                    )
                }

                request = builder.build()
                return try {
                    Log.d("log", "success")
                    val response = chain.proceed(request)
                    if (response.code == 401) {
                        Authenticated.destroySession()
                        //val context = MemberApp.getContext()
                        //val intent = Intent(context, LoginActivity::class.java)
                        //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        //context.startActivity(intent)
                    }
                    response
                } catch (e: Exception) {
                    Log.d("log", "fail")
                    Authenticated.destroySession()
                    val nullBuilder = request.newBuilder()
                    val nullRequest = nullBuilder.build()
                    val nullReponse = chain.proceed(nullRequest)
                    nullReponse
                }
            }
        })

        val logger = HttpLoggingInterceptor()
        logger.setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.addInterceptor(logger)

        if (BuildConfig.DEBUG){
            builder.addNetworkInterceptor(StethoInterceptor())
        }

        return builder.build()
    }

    private fun iniRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create())
            .build()
    }

    private fun iniRetrofitLocal(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_LOCAL_URL)
            .client(client)
            .addConverterFactory(
                GsonConverterFactory.create())
            .build()
    }

    fun <T> createService(service: Class<T>?): T {
        return retrofit.create(service)
    }

    fun <T> createLocalService(service: Class<T>?): T {
        return retrofitLocal.create(service)
    }

    fun getRetrofit(): Retrofit? {
        return retrofit
    }

    fun getBaseUrl(): String? {
        return BASE_URL
    }

}