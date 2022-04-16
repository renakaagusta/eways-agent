package com.eways.agent.order.activity.getcustomer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.dashboard.activity.MainActivity
import com.eways.agent.utils.MoneyUtils
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.proyek.infrastructures.inventory.service.usecases.GetServiceDetail
import com.proyek.infrastructures.order.order.network.body.LaporanKerusakanBody
import com.proyek.infrastructures.order.order.usecases.CreateLaporanKerusakanOrder
import com.proyek.infrastructures.user.agent.entities.UserAgent
import kotlinx.android.synthetic.main.activity_getcustomer_gantipaket_form.*
import kotlinx.android.synthetic.main.activity_getcustomer_pickupticket_form_confirmation.*
import kotlinx.android.synthetic.main.activity_getcustomer_pickupticket_form_confirmation.tvSubmit

class GetCustomerPickupTicketFormConfirmationActivity : BaseActivity() {

    private lateinit var createLaporanKerusakanOrder: CreateLaporanKerusakanOrder
    private lateinit var getServiceDetail: GetServiceDetail

    private lateinit var body: LaporanKerusakanBody
    private lateinit var agent: UserAgent

    private lateinit var customerName: String
    private lateinit var customerAddress: String
    private lateinit var customerPhoneNumber: String
    private lateinit var customerCluster: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getcustomer_psb_form_confirmation)
        CustomSupportActionBar.setCustomActionBar(this, "Detail Laporan Kerusakan")

        customerName = intent.getStringExtra("customerName")
        customerAddress = intent.getStringExtra("customerAddress")
        customerPhoneNumber = intent.getStringExtra("customerPhoneNumber")
        customerCluster = intent.getStringExtra("customerCluster")

        createLaporanKerusakanOrder = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(CreateLaporanKerusakanOrder::class.java)
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

        tvServicePacketName.text = body.internetService.name
        tvServicePacketDescription.text = body.internetService.description

        tvAgentName.text = agent.username
        showProgress()
        getServiceDetail.set("1apPg8GAOei7bEsom7fxB0wBgxD")
        getServiceDetail.get().observe(this, Observer {
            dismissProgress()
            tvAgentFee.text = MoneyUtils.getAmountString(it[0].agentFee)
        })
    }


    private fun moveToMainActivity() {
        createLaporanKerusakanOrder.get().removeObservers(this)
        this@GetCustomerPickupTicketFormConfirmationActivity.dismissProgress()
        val intent = Intent(this@GetCustomerPickupTicketFormConfirmationActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun onConfirmation(){
        tvSubmit.setOnClickListener {
            this@GetCustomerPickupTicketFormConfirmationActivity.showProgress()
            createLaporanKerusakanOrder.set(body, this@GetCustomerPickupTicketFormConfirmationActivity)
            createLaporanKerusakanOrder.get().observe(this, Observer{
                this@GetCustomerPickupTicketFormConfirmationActivity.dismissProgress()
                if(it.errors.message?.isEmpty()!!) {
                    this@GetCustomerPickupTicketFormConfirmationActivity.showSuccess("Order dikonfirmasi")
                    this@GetCustomerPickupTicketFormConfirmationActivity.setConfirm(this@GetCustomerPickupTicketFormConfirmationActivity::moveToMainActivity)
                } else {
                    this@GetCustomerPickupTicketFormConfirmationActivity.showError(it.errors.message!![0])
                }
            })
        }
    }
}