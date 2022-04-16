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
import kotlinx.android.synthetic.main.activity_profile_update_address.tvSubmit
import kotlinx.android.synthetic.main.activity_profile_update_email.*

class ProfilUpdateEmailActivity : BaseActivity(){

    private lateinit var user: UserAgent
    private lateinit var updateAgent: UpdateAgent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_update_email)
        CustomSupportActionBar.setCustomActionBar(this,"Edit Profil")

        updateAgent = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UpdateAgent::class.java)
    }

    override fun onStart() {
        super.onStart()
        user = intent.getParcelableExtra("user")
        tietEmail.setText(user.email.toString())

        tvSubmit.setOnClickListener{
            user.email = tietEmail.text.toString()
            updateSubmit()
        }
    }

    private fun updateSubmit() {
        if (tietEmail.text.toString().contains("@")) {
            updateAgent.set(
                user.ID!!,
                user.username!!,
                user.fullname!!,
                user.phoneNumber!!,
                user.email!!,
                user.password!!,
                user.address!!,
                user.agent?.nik!!,
                this@ProfilUpdateEmailActivity,
                user.agent?.employee_id!!
            )
            updateAgent.get().observe(this, Observer {
                if (it.message == "success") {
                    Authenticated.invalidateCache()
                    finish()
                }
            }
            )
        } else {
            showError("Format email tidak sesuai")
        }
    }
}