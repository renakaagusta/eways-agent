package com.eways.agent.dashboard.activity

import android.content.Intent
import android.os.Bundle
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.user.activity.LoginActivity
import com.eways.agent.user.activity.RegisterActivity
import com.eways.agent.utils.firebase.firestore.Firestore
import com.proyek.infrastructures.utils.Authenticated
import com.eways.agent.utils.firebase.messaging.FirebaseCloudMessaging
import com.proyek.infrastructures.utils.retrofit.MyRetrofit
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.activity_welcome.tvLogin

class WelcomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        Firestore.firestoreInstance.clearPersistence()

        supportActionBar?.hide()
        MyRetrofit.context = this
        Authenticated.init(this)
        FirebaseCloudMessaging.getToken()

        if(Authenticated.isValidCacheMember())
            startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))

        moveToRegister()
        moveToLogin()
    }

    private fun moveToRegister(){
        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun moveToLogin(){
        tvLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
