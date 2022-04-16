package com.eways.agent.order.activity.getcustomer

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.utils.Utils
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.utils.Authenticated
import kotlinx.android.synthetic.main.activity_getcustomer_layananbebas_form.*

class GetCustomerLayananBebasFormActivity : BaseActivity(){
    private var layananBebasName : String? =null
    private var layananBebasDetail : String? =null

    private lateinit var agentId: String
    private lateinit var agent: UserAgent

    private lateinit var customerName: String
    private lateinit var customerAddress: String
    private lateinit var customerPhoneNumber: String
    private lateinit var customerCluster: String

    private lateinit var user: UserAgent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getcustomer_layananbebas_form)
        CustomSupportActionBar.setCustomActionBar(this, "Layanan Bebas")

        customerName = intent.getStringExtra("customerName")
        customerAddress = intent.getStringExtra("customerAddress")
        customerPhoneNumber = intent.getStringExtra("customerPhoneNumber")
        customerCluster = intent.getStringExtra("customerCluster")

        user = Authenticated.getUserAgent()

        submissionButton()
        formCompleteness()
    }


    private fun isValid():Boolean{
        return Utils.isNotNullOrEmpty(layananBebasName) &&
                Utils.isNotNullOrEmpty(layananBebasDetail)
    }

    private fun formCompleteness(){
        Utils.setOnTextChanged(etLayananBebasName, object : Utils.Companion.OnTextChanged{
            override fun onChange(text: String) {
                layananBebasName = text
            }
        }){this.submissionButton()}
        Utils.setOnTextChanged(etLayananBebasDetail, object : Utils.Companion.OnTextChanged{
            override fun onChange(text: String) {
                layananBebasDetail = text
            }
        }){this.submissionButton()}
    }


    private fun submissionButton(){
        if(!isValid()){
            tvSubmit.isClickable = false
            tvSubmit.background = ContextCompat.getDrawable(this@GetCustomerLayananBebasFormActivity, R.drawable.rc_bglightgray)
        }else{
            tvSubmit.background = ContextCompat.getDrawable(this@GetCustomerLayananBebasFormActivity, R.drawable.rc_bgprimary)
            tvSubmit.isClickable = true
            moveToLayananBebasFormConfirmation()
        }
    }

    private fun moveToLayananBebasFormConfirmation(){
        tvSubmit.setOnClickListener {
            val intent = Intent(this, GetCustomerLayananBebasFormConfirmationActivity::class.java)
            intent.putExtra("layananBebasName", etLayananBebasName.text.toString())
            intent.putExtra("layananBebasDetail", etLayananBebasDetail.text.toString())
            intent.putExtra("agent", user)
            intent.putExtra("customerName", customerName)
            intent.putExtra("customerAddress", customerAddress)
            intent.putExtra("customerPhoneNumber", customerPhoneNumber)
            intent.putExtra("customerCluster", customerCluster)
            startActivity(intent)
        }
    }
}