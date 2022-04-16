package com.eways.agent.user.activity

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.user.agent.usecases.GetAgentList
import com.proyek.infrastructures.user.agent.usecases.UpdateAgent
import com.proyek.infrastructures.utils.Authenticated
import kotlinx.android.synthetic.main.activity_profile_update_employeeid.*
import kotlinx.android.synthetic.main.activity_profile_update_employeeid.tvSubmit

class ProfileUpdateEmployeeIDActivity : BaseActivity(){

    private lateinit var user: UserAgent
    private lateinit var updateAgent: UpdateAgent
    private lateinit var getAgentList: GetAgentList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_update_employeeid)
        CustomSupportActionBar.setCustomActionBar(this,"Edit Profil")

        updateAgent = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UpdateAgent::class.java)
        getAgentList = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetAgentList::class.java)

    }

    override fun onStart() {
        super.onStart()
        user = intent.getParcelableExtra("user")
        tietEmployeeID.setText(user.agent?.employee_id.toString())

        tvSubmit.setOnClickListener{
            if(tietEmployeeID.text.toString()==user.agent?.employee_id) {
                updateSubmit()
                return@setOnClickListener
            }
            var unique = true
            getAgentList.set()
            getAgentList.get().observe(this, Observer{
                it.data.forEach {
                    if(it.agent?.employee_id == tietEmployeeID.text.toString()) {
                        unique = false
                    }
                }

                if(unique) {
                    user.agent?.employee_id = tietEmployeeID.text.toString()
                    updateSubmit()
                } else {
                    showError("ID karyawan telah terdaftar")
                }
            })
        }
    }
    private fun updateSubmit() {
        updateAgent.set(user.ID!!,
            user.username!!,
            user.fullname!!,
            user.phoneNumber!!,
            user.email!!,
            user.password!!,
            user.address!!,
            user.agent?.nik!!,
            this@ProfileUpdateEmployeeIDActivity,
            user.agent?.employee_id!!)
            updateAgent.get().observe(this, Observer{
                if(it.message=="success"){
                    Authenticated.invalidateCache()
                    finish()
                }
            }
        )
    }
}