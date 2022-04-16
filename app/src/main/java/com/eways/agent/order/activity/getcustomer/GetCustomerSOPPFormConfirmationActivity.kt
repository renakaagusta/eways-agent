package com.eways.agent.order.activity.getcustomer

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.dashboard.activity.MainActivity
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.proyek.infrastructures.order.order.network.body.SOPPBody
import com.proyek.infrastructures.order.order.usecases.CreateSOPPOrder
import com.proyek.infrastructures.user.agent.entities.UserAgent
import kotlinx.android.synthetic.main.activity_getcustomer_sopp_form_confirmation.*

class GetCustomerSOPPFormConfirmationActivity :BaseActivity(){

    private lateinit var createSOPPOrder: CreateSOPPOrder

    private lateinit var body: SOPPBody
    private lateinit var agent: UserAgent

    private lateinit var customerName: String
    private lateinit var customerAddress: String
    private lateinit var customerPhoneNumber: String
    private lateinit var customerCluster: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getcustomer_sopp_form_confirmation)
        CustomSupportActionBar.setCustomActionBar(this, "Detail SOPP")

        customerName = intent.getStringExtra("customerName")
        customerAddress = intent.getStringExtra("customerAddress")
        customerPhoneNumber = intent.getStringExtra("customerPhoneNumber")
        customerCluster = intent.getStringExtra("customerCluster")

        createSOPPOrder = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(CreateSOPPOrder::class.java)

        body = intent.getParcelableExtra("body")
        agent = intent.getParcelableExtra("agent")


        setData()
        onConfirmation()
    }

    private fun setData(){
        tvCustomerName.text = customerName
        tvCustomerAddress.text = customerAddress
        tvCustomerPhone.text = customerPhoneNumber
        tvCustomerCluster.text = customerCluster

        tvAgentName.text = agent.username

        tvCustomerNameSOPP.text = customerName
        tvCustomerAddressSOPP.text = customerAddress
        tvCustomerPhoneSOPP.text = customerPhoneNumber
        tvCustomerCluster.text = customerCluster

        tvSOPPName.text = body.invoice.name
        tvAdditionalInfo.text = body.description

    }


    private fun moveToMainActivity() {
        createSOPPOrder.get().removeObservers(this)
        this@GetCustomerSOPPFormConfirmationActivity.dismissProgress()
        val intent = Intent(this@GetCustomerSOPPFormConfirmationActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun onConfirmation(){
        tvSubmit.setOnClickListener {
            this@GetCustomerSOPPFormConfirmationActivity.showProgress()
            createSOPPOrder.set(body, this@GetCustomerSOPPFormConfirmationActivity)
            createSOPPOrder.get().observe(this, Observer{
                this@GetCustomerSOPPFormConfirmationActivity.dismissProgress()
                if(it.errors.message?.isEmpty()!!) {
                    this@GetCustomerSOPPFormConfirmationActivity.showSuccess("Order dikonfirmasi")
                    this@GetCustomerSOPPFormConfirmationActivity.setConfirm(this@GetCustomerSOPPFormConfirmationActivity::moveToMainActivity)
                } else {
                    this@GetCustomerSOPPFormConfirmationActivity.showError(it.errors.message!![0])
                }
            })

        }
    }
}