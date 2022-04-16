package com.eways.agent.user.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.dashboard.activity.MainActivity
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import kotlinx.android.synthetic.main.activity_register_otp.*


class RegisterOTPActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_otp)
        CustomSupportActionBar.setCustomActionBar(this,"Registrasi")

        moveToMain()
        disableConfirmation()
    }

    fun disableConfirmation(){
        tvConfirmation.isClickable = false
    }

    private fun moveToMain(){
        otpRegister.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                when(p0?.length){
                    4 -> {
                        tvConfirmation.setTextColor(ContextCompat.getColor(this@RegisterOTPActivity, R.color.white))
                        tvConfirmation.setBackgroundColor(ContextCompat.getColor(this@RegisterOTPActivity, R.color.colorPrimary))
                        tvConfirmation.isClickable
                        tvConfirmation.setOnClickListener {
                            val intent = Intent(this@RegisterOTPActivity, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            finish()
                        }
                    }
                    else ->{
                        tvConfirmation.setTextColor(ContextCompat.getColor(this@RegisterOTPActivity, R.color.colorRegularText))
                        tvConfirmation.setBackgroundColor(ContextCompat.getColor(this@RegisterOTPActivity, R.color.lightGrey))
                        disableConfirmation()
                    }
                }
            }
        })
    }
}