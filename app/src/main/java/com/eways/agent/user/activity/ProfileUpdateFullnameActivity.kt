package com.eways.agent.user.activity

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.user.agent.usecases.UpdateAgent
import com.proyek.infrastructures.utils.Authenticated
import kotlinx.android.synthetic.main.activity_profile_update_fullname.*
import kotlinx.android.synthetic.main.activity_profile_update_fullname.tvSubmit

class ProfileUpdateFullnameActivity :BaseActivity() {
    private lateinit var user: UserAgent
    private lateinit var updateAgent: UpdateAgent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_update_fullname)
        CustomSupportActionBar.setCustomActionBar(this,"Edit Profil")

        updateAgent = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UpdateAgent::class.java)

        tvSubmit.setOnClickListener{
            user.fullname = tietFullname.text.toString()
            updateSubmit()
        }
    }

    override fun onStart() {
        super.onStart()

        user = intent.getParcelableExtra("user")
        tietFullname.setText(user.fullname.toString())

        tvSubmit.setOnClickListener{
            user.fullname = tietFullname.text.toString()
            updateSubmit()
        }
    }

    private fun updateSubmit() {
        if(tietFullname.text.toString().length > 7) {
            updateAgent.set(
                user.ID!!,
                user.username!!,
                user.fullname!!,
                user.phoneNumber!!,
                user.email!!,
                user.password!!,
                user.address!!,
                user.agent?.nik!!,
                this@ProfileUpdateFullnameActivity,
                user.agent?.employee_id!!
            )
            updateAgent.get().observe(this, Observer {
                Log.d("message", it.toString())
                if (it.message == "success") {
                    Authenticated.invalidateCache()
                    finish()
                }
            }
            )
        } else {
            showError("Panjang username kurang dari 8 karakter")
        }
    }
}