package com.eways.agent.user.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.dashboard.activity.MainActivity
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.user.agent.usecases.LoginAgent
import com.eways.agent.utils.firebase.firestore.Firestore
import com.eways.agent.utils.firebase.messaging.FirebaseCloudMessaging
import com.proyek.infrastructures.user.agent.usecases.SendOTP
import com.proyek.infrastructures.utils.Authenticated
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity() {
    private lateinit var loginAgent: LoginAgent

    private lateinit var sendOTP: SendOTP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        CustomSupportActionBar.setCustomActionBar(this, "Login")

        sendOTP =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(SendOTP::class.java)

        loginAgent = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(LoginAgent::class.java)
        FirebaseCloudMessaging.getToken()
        checkLogin()
    }

    private fun moveToLoginOTP(phoneNumber: String, code: String,nik: String, token: String) {
        val intent = Intent(this@LoginActivity, LoginOTPActivity::class.java)
        intent.putExtra("phoneNumber", phoneNumber)
        intent.putExtra("nik", nik)
        intent.putExtra("code", code)
        intent.putExtra("token", token)
        Log.d("code", code)
        startActivity(intent)
    }

    private fun moveToMain(agent: UserAgent) {
        val intent = Intent(this@LoginActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        Authenticated.setUserAgent(agent)
        startActivity(intent)
        finish()
    }

    private fun checkLogin(){
        tvLogin.setOnClickListener {
            if(tietPhone.text.toString().isEmpty())
                showError("Masukan Nomor HP yang terdaftar")
            else if(tietEmployeeID.text.toString().isEmpty())
                showError("Masukan NIK yang terdaftar")
            else {
                this@LoginActivity.showProgress()
                GlobalScope.launch(Dispatchers.Main) {
                    loginAgent.get().removeObservers(this@LoginActivity)
                    loginAgent.set(
                        tietPhone.text.toString(),
                        tietEmployeeID.text.toString(),
                        Firestore.token,
                        0,
                        this@LoginActivity
                    )
                    delay(2000)
                    loginAgent.get().observe(this@LoginActivity, Observer { items ->
                        this@LoginActivity.dismissProgress()
                        if (items.message == "success") {
                            moveToMain(items.data.user)
                            finish()
                        } else {
                            this@LoginActivity.dismissProgress()
                            if((items.errors.message!![0] == "OTP status false, please do OTP first") || (items.errors.message!![0] == "Agent OTP data not found")) {
                                val allowedChars = ('0'..'9')
                                var code = (1..6)
                                    .map { allowedChars.random() }
                                    .joinToString("")

                                var phoneNumber = tietPhone.text.toString()

                                sendOTP.set(phoneNumber, code)
                                sendOTP.get().observe(this@LoginActivity, Observer {
                                    moveToLoginOTP(tietPhone.text.toString(), code,tietEmployeeID.text.toString(), Firestore.token)
                                })
                            } else {
                                this@LoginActivity.showError(items.errors.message!![0])
                            }
                        }
                    })
                    loginAgent.get().removeObservers(this@LoginActivity)
                }
            }
        }
    }
}