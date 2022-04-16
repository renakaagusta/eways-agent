package com.eways.agent.order.activity.getcustomer

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.dashboard.activity.MainActivity
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.proyek.infrastructures.order.order.usecases.CreateTitipPaketOrder
import com.proyek.infrastructures.user.agent.entities.UserAgent
import kotlinx.android.synthetic.main.activity_getcustomer_titippaket_form_confirmation.*

class GetCustomerTitipPaketFormConfirmationActivity : BaseActivity(){
    private lateinit var createTitipPaketOrder: CreateTitipPaketOrder

    private lateinit var agent: UserAgent

    private lateinit var customerName: String
    private lateinit var customerAddress: String
    private lateinit var customerPhoneNumber: String
    private lateinit var customerCluster: String

    private lateinit var itemName: String
    private lateinit var itemDescription: String
    private lateinit var senderName: String
    private lateinit var senderPhoneNumber: String
    private lateinit var senderAddress: String
    private lateinit var receiverName: String
    private lateinit var receiverPhoneNumber: String
    private lateinit var receiverAddress: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getcustomer_titippaket_form_confirmation)
        CustomSupportActionBar.setCustomActionBar(this, "Detail Titip Paket")


        createTitipPaketOrder = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(CreateTitipPaketOrder::class.java)

        agent = intent.getParcelableExtra("agent")

        customerName = intent.getStringExtra("customerName")
        customerAddress = intent.getStringExtra("customerAddress")
        customerPhoneNumber = intent.getStringExtra("customerPhoneNumber")
        customerCluster = intent.getStringExtra("customerCluster")

        createTitipPaketOrder = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(CreateTitipPaketOrder::class.java)

        itemName = intent.getStringExtra("packageName")
        itemDescription = intent.getStringExtra("packageDescription")

        senderName = intent.getStringExtra("senderName")
        senderPhoneNumber = intent.getStringExtra("senderPhoneNumber")
        senderAddress = intent.getStringExtra("senderAddress")

        receiverName = intent.getStringExtra("receiverName")
        receiverPhoneNumber = intent.getStringExtra("receiverPhoneNumber")
        receiverAddress = intent.getStringExtra("receiverAddress")


        setData()
        onConfirmation()
    }

    private fun setData(){
        tvReceiverName.text = receiverName
        tvReceiverAddress.text = receiverAddress
        tvReceiverPhone.text = receiverAddress

        tvSenderName.text = senderName
        tvSenderAddress.text = senderAddress
        tvSenderPhone.text= senderPhoneNumber

        tvItemName.text = itemName
        tvItemDescription.text = itemDescription
    }

    private fun moveToMainActivity() {
        createTitipPaketOrder.get().removeObservers(this)
        this@GetCustomerTitipPaketFormConfirmationActivity.dismissProgress()
        val intent = Intent(this@GetCustomerTitipPaketFormConfirmationActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun onConfirmation(){
        tvSubmit.setOnClickListener {
            this@GetCustomerTitipPaketFormConfirmationActivity.showProgress()
            createTitipPaketOrder.set(
                senderName, senderPhoneNumber,  senderAddress,
                receiverName, receiverPhoneNumber, receiverAddress,
                itemName, itemDescription,
                "1apLWI7YYVugJAKSsrhvcOHDyHZ", agent.ID!!,
                customerName, customerAddress, customerPhoneNumber, customerCluster,
                this@GetCustomerTitipPaketFormConfirmationActivity)
                createTitipPaketOrder.get().observe(this, Observer{
                this@GetCustomerTitipPaketFormConfirmationActivity.dismissProgress()
                if(it.errors.message?.isEmpty()!!) {
                    this@GetCustomerTitipPaketFormConfirmationActivity.showSuccess("Order dikonfirmasi")
                    this@GetCustomerTitipPaketFormConfirmationActivity.setConfirm(this@GetCustomerTitipPaketFormConfirmationActivity::moveToMainActivity)
                } else {
                    this@GetCustomerTitipPaketFormConfirmationActivity.showError(it.errors.message!![0])
                }
            })

        }
    }
}