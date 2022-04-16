package com.eways.agent.order.activity.getcustomer

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.dashboard.activity.MainActivity
import com.eways.agent.utils.MoneyUtils
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.proyek.infrastructures.inventory.service.usecases.GetServiceDetail
import com.proyek.infrastructures.order.order.network.body.PSBBody
import com.proyek.infrastructures.order.order.usecases.CreatePSBOrder
import com.proyek.infrastructures.user.agent.entities.UserAgent
import kotlinx.android.synthetic.main.activity_getcustomer_psb_form_confirmation.*

class GetCustomerPSBFormConfirmationActivity : BaseActivity() {
    private lateinit var createPSBOrder: CreatePSBOrder
    private lateinit var getServiceDetail: GetServiceDetail

    private lateinit var body: PSBBody
    private lateinit var agent: UserAgent

    private lateinit var customerName: String
    private lateinit var customerAddress: String
    private lateinit var customerPhoneNumber: String
    private lateinit var customerCluster: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getcustomer_psb_form_confirmation)
        CustomSupportActionBar.setCustomActionBar(this, "Detail Pasang Baru")

        customerName = intent.getStringExtra("customerName")
        customerAddress = intent.getStringExtra("customerAddress")
        customerPhoneNumber = intent.getStringExtra("customerPhoneNumber")
        customerCluster = intent.getStringExtra("customerCluster")

        createPSBOrder = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(CreatePSBOrder::class.java)
        getServiceDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetServiceDetail::class.java)

        body = intent.getParcelableExtra("body")
        agent = intent.getParcelableExtra("agent")

        setData()
        onConfirmation()
    }


    private fun setData() {
        tvCustomerName.text = customerName
        tvCustomerAddress.text = customerAddress
        tvCustomerPhone.text = customerPhoneNumber
        tvCustomerCluster.text = customerCluster

        tvServicePacketName.text = body.internetService.name
        tvServicePacketDescription.text = body.internetService.description

        tvAgentName.text = agent.username
        showProgress()
        getServiceDetail.set("1apQwniIJo6WnAm2cIk7WEkzWt0")
        getServiceDetail.get().observe(this, Observer {
            dismissProgress()
            tvAgentFee.text = MoneyUtils.getAmountString(it[0].agentFee)
        })
    }


    private fun moveToMainActivity() {
        createPSBOrder.get().removeObservers(this)
        this@GetCustomerPSBFormConfirmationActivity.dismissProgress()
        val intent = Intent(this@GetCustomerPSBFormConfirmationActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun onConfirmation() {
        tvSubmit.setOnClickListener {
            this@GetCustomerPSBFormConfirmationActivity.showProgress()
            createPSBOrder.set(body, this@GetCustomerPSBFormConfirmationActivity)
            createPSBOrder.get().observe(this, Observer{
                this@GetCustomerPSBFormConfirmationActivity.dismissProgress()
                if(it.errors.message?.isEmpty()!!) {
                    this@GetCustomerPSBFormConfirmationActivity.showSuccess("Order dikonfirmasi")
                    this@GetCustomerPSBFormConfirmationActivity.setConfirm(this@GetCustomerPSBFormConfirmationActivity::moveToMainActivity)
                } else {
                    this@GetCustomerPSBFormConfirmationActivity.showError(it.errors.message!![0])
                }
            })
        }
    }
}