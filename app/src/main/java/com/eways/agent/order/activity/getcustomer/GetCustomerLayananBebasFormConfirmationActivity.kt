package com.eways.agent.order.activity.getcustomer

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.dashboard.activity.MainActivity
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.proyek.infrastructures.inventory.service.usecases.GetServiceDetail
import com.proyek.infrastructures.order.order.usecases.CreateLayananBebasOrder
import com.proyek.infrastructures.user.agent.entities.UserAgent
import kotlinx.android.synthetic.main.activity_getcustomer_layananbebas_form_confirmation.*

class GetCustomerLayananBebasFormConfirmationActivity : BaseActivity(){
    private lateinit var createLayananBebasOrder: CreateLayananBebasOrder
    private lateinit var agent: UserAgent

    private lateinit var customerName: String
    private lateinit var customerAddress: String
    private lateinit var customerPhoneNumber: String
    private lateinit var customerCluster: String

    private lateinit var layananBebasName: String
    private lateinit var layananBebasDetail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getcustomer_layananbebas_form_confirmation)
        CustomSupportActionBar.setCustomActionBar(this, "Detail Layanan Bebas")

        layananBebasName = intent.getStringExtra("layananBebasName")
        layananBebasDetail = intent.getStringExtra("layananBebasDetail")

        customerName = intent.getStringExtra("customerName")
        customerAddress = intent.getStringExtra("customerAddress")
        customerPhoneNumber = intent.getStringExtra("customerPhoneNumber")
        customerCluster = intent.getStringExtra("customerCluster")

        createLayananBebasOrder = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(CreateLayananBebasOrder::class.java)


        agent = intent.getParcelableExtra("agent")
        setData()
        onConfirmation()
    }

    private fun setData(){
        tvCustomerName.text = customerName
        tvCustomerPhone.text = customerPhoneNumber
        tvCustomerAddress.text = customerAddress
        tvCustomerCluster.text = customerCluster

        tvLayananBebasName.text = layananBebasName
        tvLayananBebasDetail.text = layananBebasDetail

        tvAgentName.text = agent.username
    }


    private fun moveToMainActivity() {
        createLayananBebasOrder.get().removeObservers(this)
        this@GetCustomerLayananBebasFormConfirmationActivity.dismissProgress()
        val intent = Intent(this@GetCustomerLayananBebasFormConfirmationActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun onConfirmation(){
        tvSubmit.setOnClickListener {
            this@GetCustomerLayananBebasFormConfirmationActivity.showProgress()
            createLayananBebasOrder.set(layananBebasName,
                layananBebasDetail,
                "1apSvFBkIq0ScrIeQWX8cGl3T8X",
                customerName,
                customerAddress,
                customerPhoneNumber,
                customerCluster,
                agent.ID!!,
            this@GetCustomerLayananBebasFormConfirmationActivity)
            createLayananBebasOrder.get().observe(this, Observer{
                this@GetCustomerLayananBebasFormConfirmationActivity.dismissProgress()
                if(it.errors.message?.isEmpty()!!) {
                    this@GetCustomerLayananBebasFormConfirmationActivity.showSuccess("Order dikonfirmasi")
                    this@GetCustomerLayananBebasFormConfirmationActivity.setConfirm(this@GetCustomerLayananBebasFormConfirmationActivity::moveToMainActivity)
                } else {
                    this@GetCustomerLayananBebasFormConfirmationActivity.showError(it.errors.message!![0])
                }
            })

        }
    }
}