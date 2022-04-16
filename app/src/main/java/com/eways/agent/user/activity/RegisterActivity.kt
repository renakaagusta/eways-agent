package com.eways.agent.user.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.dashboard.activity.WelcomeActivity
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.proyek.infrastructures.user.agent.usecases.CreateAgent
import com.proyek.infrastructures.user.agent.usecases.GetAgentList
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_welcome.tvRegister

class RegisterActivity : BaseActivity(){

    private lateinit var createAgent: CreateAgent
    private lateinit var getAgentList: GetAgentList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        CustomSupportActionBar.setCustomActionBar(this,"Registrasi")

        createAgent = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(CreateAgent::class.java)
        getAgentList = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetAgentList::class.java)

        checkUserNameLength()
    }

    private fun checkUserNameLength() {
        tvRegister.setOnClickListener {
            if (tietFullname.text.toString().length < 8)
                showError("Panjang nama kurang dari 8 karakter")
            else
                checkEmailFormat()
        }
    }

    private fun checkEmailFormat() {
        if((tietEmail.text.toString().contains("@")==false) || (tietEmail.text.toString().contains(".")==false))
            showError("Format email tidak sesuai")
        else
            checkNIKLength()
    }

    private fun checkNIKLength() {
        if(tietNIK.text.toString().length!=16)
            showError("Panjang NIK tidak sesuai")
        else
            checkUnique()
    }

    private fun checkUnique() {
        showProgress()

        getAgentList.set()
        getAgentList.get().observe(this, Observer { items ->
            var unique = true

            items.data.forEach {
                if(it.username == tietFullname.text.toString()) {
                    unique = false
                    dismissProgress()
                    showError("Username tersebut telah terdaftar")
                }
                if(it.email == tietEmail.text.toString()) {
                    unique = false
                    dismissProgress()
                    showError("Email tersebut telah terdaftar")
                }
                if(it.agent?.nik == tietNIK.text.toString()) {
                    unique = false
                    dismissProgress()
                    showError("NIK tersebut telah terdaftar")
                }
                if(it.agent?.employee_id == tietEmployeeID.text.toString()) {
                    unique = false
                    dismissProgress()
                    showError("ID pegawai tersebut telah terdaftar")
                }
            }

            if(unique)
                moveToRegisterOTP()
        })
    }

    private fun moveToWelcomeActivity() {
        val intent = Intent(this@RegisterActivity, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun moveToRegisterOTP(){
            createAgent.set(tietFullname.text.toString(), tietFullname.text.toString(), "+62"+tietPhone.text.toString(), tietEmail.text.toString(), "ini_password", tietAddress.text.toString(), tietNIK.text.toString(), this@RegisterActivity, tietEmployeeID.text.toString())
            createAgent.get().observe(this, Observer { items ->
                this@RegisterActivity.dismissProgress()
                if(items.errors.message?.isEmpty()!!) {
                    showSuccess("Akun berhasil dibuat")
                    setConfirm(this::moveToWelcomeActivity)
                } else {
                    this@RegisterActivity.showError(items.errors.message!![0])
                }
            })
    }
}