package com.eways.agent.user.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import kotlinx.android.synthetic.main.activity_login_otp.*
import kotlinx.android.synthetic.main.activity_register_otp.tvConfirmation
import com.eways.agent.dashboard.activity.MainActivity
import com.proyek.infrastructures.user.agent.usecases.LoginAgent
import com.proyek.infrastructures.utils.Authenticated
import java.util.concurrent.TimeUnit

class LoginOTPActivity : BaseActivity(){
    private lateinit var loginAgent: LoginAgent

    private lateinit var verificationNumber: String

    private lateinit var phoneNumber: String
    private lateinit var nik: String
    private lateinit var token: String
    private lateinit var code: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_otp)
        CustomSupportActionBar.setCustomActionBar(this,"Login")

        loginAgent = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(LoginAgent::class.java)

        phoneNumber = intent.getStringExtra("phoneNumber")!!
        nik = intent.getStringExtra("nik")!!
        code = intent.getStringExtra("code")!!
        token = intent.getStringExtra("token")!!


        disableConfirmation()
        setButtonMethod()
    }


    fun authenticate (){
        if(verificationNumber == code) {
            successLoginOTP()
        } else {
            this@LoginOTPActivity.showError("Kode yang anda anda masukan salah")
        }
    }

    fun disableConfirmation(){
        tvConfirmation.isClickable = false
    }

    private fun successLoginOTP() {
        this@LoginOTPActivity.showProgress()
        loginAgent.set(phoneNumber, nik, token, 1, this@LoginOTPActivity)
        loginAgent.get().observe(this, Observer {
            this@LoginOTPActivity.dismissProgress()
            if(it.errors.message?.isEmpty()!!) {
                Authenticated.setUserAgent(it.data.user)
                val intent = Intent(this@LoginOTPActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
            } else {
                this@LoginOTPActivity.showError(it.errors.message!![0])
            }
        })
    }

    private fun setButtonMethod(){
        otpLogin.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                when(p0?.length){
                    6 -> {
                        tvConfirmation.setTextColor(ContextCompat.getColor(this@LoginOTPActivity, R.color.white))
                        tvConfirmation.setBackgroundColor(ContextCompat.getColor(this@LoginOTPActivity, R.color.colorPrimary))
                        tvConfirmation.isClickable
                        tvConfirmation.setOnClickListener {
                            verificationNumber = otpLogin.text.toString()
                            authenticate()
                        }
                    }
                    else ->{
                        tvConfirmation.setTextColor(ContextCompat.getColor(this@LoginOTPActivity, R.color.colorRegularText))
                        tvConfirmation.setBackgroundColor(ContextCompat.getColor(this@LoginOTPActivity, R.color.lightGrey))
                        disableConfirmation()
                    }
                }
            }
        })
    }
}