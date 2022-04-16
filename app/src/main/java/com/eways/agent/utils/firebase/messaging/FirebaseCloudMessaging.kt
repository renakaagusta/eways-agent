package com.eways.agent.utils.firebase.messaging

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.eways.agent.utils.firebase.firestore.Firestore
import com.proyek.infrastructures.utils.retrofit.MyRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object FirebaseCloudMessaging {

    fun getToken() {
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
            Firestore.token = it.token
        }.addOnFailureListener {
            Log.d("fcm", it.message)
        }
    }

    fun pushNotif(title: String, body: String, token: String) {
        val services = NotificationApiServices::class.java
        MyRetrofit.createService(services).pushNotification(title, body, token).enqueue(object :
            Callback<String?> {
            override fun onFailure(call: Call<String?>?, t: Throwable?) {
                Log.d("error", t.toString())
            }

            override fun onResponse(call: Call<String?>?, response: Response<String?>?) {
                if (response!!.isSuccessful) {
                    Log.d("response", response.body()!!)
                } else {
                    Log.d("response", response.code().toString())
                }
            }
        })

    }

}
