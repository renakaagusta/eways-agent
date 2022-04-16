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
import com.proyek.infrastructures.order.order.network.body.GantiPaketBody
import com.proyek.infrastructures.order.order.usecases.CreateGantiPaketOrder
import com.proyek.infrastructures.user.agent.entities.UserAgent
import kotlinx.android.synthetic.main.activity_getcustomer_gantipaket_form_confirmation.*

class GetCustomerGantiPaketFormConfirmationActivity : BaseActivity(){
    private lateinit var createGantiPaketOrder: CreateGantiPaketOrder
    private lateinit var getServiceDetail: GetServiceDetail

    private lateinit var body: GantiPaketBody
    private lateinit var agent: UserAgent

    private lateinit var customerName: String
    private lateinit var customerAddress: String
    private lateinit var customerPhoneNumber: String
    private lateinit var customerCluster: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getcustomer_gantipaket_form_confirmation)
        CustomSupportActionBar.setCustomActionBar(this, "Detail Ganti Paket")
        customerName = intent.getStringExtra("customerName")
        customerAddress = intent.getStringExtra("customerAddress")
        customerPhoneNumber = intent.getStringExtra("customerPhoneNumber")
        customerCluster = intent.getStringExtra("customerCluster")

        createGantiPaketOrder = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            CreateGantiPaketOrder::class.java)
        getServiceDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetServiceDetail::class.java)


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
        showProgress()
        getServiceDetail.set("1apRo3tPQuBvIvMsHvJp0VyFBiW")
        getServiceDetail.get().observe(this, Observer {
            dismissProgress()
            tvAgentFee.text = MoneyUtils.getAmountString(it[0].agentFee)
        })

        tvCurrentServicePacketName.text = body.oldInternetService.name
        tvCurrentServicePacketDescription.text = body.oldInternetService.description
        tvServicePacketName.text = body.newInternetService.name
        tvServicePacketDescription.text = body.newInternetService.description

        tvAgentName.text = agent.username
    }

    private fun moveToMainActivity() {
        createGantiPaketOrder.get().removeObservers(this)
        this@GetCustomerGantiPaketFormConfirmationActivity.dismissProgress()
        val intent = Intent(this@GetCustomerGantiPaketFormConfirmationActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun onConfirmation(){
        tvSubmit.setOnClickListener {
            this@GetCustomerGantiPaketFormConfirmationActivity.showProgress()
            createGantiPaketOrder.set(body, this@GetCustomerGantiPaketFormConfirmationActivity)
            createGantiPaketOrder.get().observe(this, Observer{
                this@GetCustomerGantiPaketFormConfirmationActivity.dismissProgress()
                if(it.errors.message?.isEmpty()!!) {
                    this@GetCustomerGantiPaketFormConfirmationActivity.showSuccess("Order dikonfirmasi")
                    this@GetCustomerGantiPaketFormConfirmationActivity.setConfirm(this@GetCustomerGantiPaketFormConfirmationActivity::moveToMainActivity)
                } else {
                    this@GetCustomerGantiPaketFormConfirmationActivity.showError(it.errors.message!![0])
                }
            })
        }
    }
}