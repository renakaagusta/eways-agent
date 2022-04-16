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
import kotlinx.android.synthetic.main.activity_getcustomer_titippaket_form.*

class GetCustomerTitipPaketFormActivity : BaseActivity(){
    private var itemName : String? =null
    private var itemDescription :String? = null
    private var senderName :String? = null
    private var senderAddress :String? = null
    private var senderPhone :String? = null
    private var receiverName :String? = null
    private var receiverAddress :String? = null
    private var receiverPhone :String? = null
    private lateinit var agentOption : String

    private lateinit var user: UserAgent

    private lateinit var customerName: String
    private lateinit var customerAddress: String
    private lateinit var customerPhoneNumber: String
    private lateinit var customerCluster: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getcustomer_titippaket_form)
        CustomSupportActionBar.setCustomActionBar(this,"Titip Paket")

        customerName = intent.getStringExtra("customerName")
        customerAddress = intent.getStringExtra("customerAddress")
        customerPhoneNumber = intent.getStringExtra("customerPhoneNumber")
        customerCluster = intent.getStringExtra("customerCluster")

        user = Authenticated.getUserAgent()

        submissionButton()
        formCompleteness()
    }

    private fun isValid():Boolean{
        return Utils.isNotNullOrEmpty(itemName) &&
                Utils.isNotNullOrEmpty(itemDescription) &&
                Utils.isNotNullOrEmpty(senderName) &&
                Utils.isNotNullOrEmpty(senderPhone) &&
                Utils.isNotNullOrEmpty(senderAddress) &&
                Utils.isNotNullOrEmpty(receiverName) &&
                Utils.isNotNullOrEmpty(receiverAddress) &&
                Utils.isNotNullOrEmpty(receiverPhone)
    }
    private fun submissionButton(){
        if(!isValid()){
            tvSubmit.isClickable = false
            tvSubmit.background = ContextCompat.getDrawable(this@GetCustomerTitipPaketFormActivity, R.drawable.rc_bglightgray)
        }else{
            tvSubmit.background = ContextCompat.getDrawable(this@GetCustomerTitipPaketFormActivity, R.drawable.rc_bgprimary)
            tvSubmit.isClickable = true
            moveToTitipPaketFormConfirmation()
        }
    }

    private fun formCompleteness(){
        Utils.setOnTextChanged(etItemName, object : Utils.Companion.OnTextChanged{
            override fun onChange(text: String) {
                itemName = text
            }
        }){this.submissionButton()}
        Utils.setOnTextChanged(etItemDescription, object : Utils.Companion.OnTextChanged{
            override fun onChange(text: String) {
                itemDescription = text
            }
        }){this.submissionButton()}
        Utils.setOnTextChanged(etSenderName, object : Utils.Companion.OnTextChanged{
            override fun onChange(text: String) {
                senderName = text
            }
        }){this.submissionButton()}
        Utils.setOnTextChanged(etSenderAddress, object : Utils.Companion.OnTextChanged{
            override fun onChange(text: String) {
                senderAddress = text
            }
        }){this.submissionButton()}
        Utils.setOnTextChanged(etSenderPhone, object : Utils.Companion.OnTextChanged{
            override fun onChange(text: String) {
                senderPhone = text
            }
        }){this.submissionButton()}
        Utils.setOnTextChanged(etReceiverName, object : Utils.Companion.OnTextChanged{
            override fun onChange(text: String) {
                receiverName = text
            }
        }){this.submissionButton()}
        Utils.setOnTextChanged(etReceiverAddress, object : Utils.Companion.OnTextChanged{
            override fun onChange(text: String) {
                receiverAddress = text
            }
        }){this.submissionButton()}
        Utils.setOnTextChanged(etReceiverPhone, object : Utils.Companion.OnTextChanged{
            override fun onChange(text: String) {
                receiverPhone = text
            }
        }){this.submissionButton()}
    }

    private fun moveToTitipPaketFormConfirmation(){
        tvSubmit.setOnClickListener {
            val intent = Intent(this, GetCustomerTitipPaketFormConfirmationActivity::class.java)
            intent.putExtra("packageName", etItemName.text.toString())
            intent.putExtra("packageDescription", etItemDescription.text.toString())
            intent.putExtra("senderName", etSenderName.text.toString())
            intent.putExtra("senderPhoneNumber", etSenderPhone.text.toString())
            intent.putExtra("senderAddress", etSenderAddress.text.toString())
            intent.putExtra("receiverName", etReceiverName.text.toString())
            intent.putExtra("receiverPhoneNumber", etReceiverPhone.text.toString())
            intent.putExtra("receiverAddress", etReceiverAddress.text.toString())

            intent.putExtra("agent", user)
            intent.putExtra("customerName", customerName)
            intent.putExtra("customerAddress", customerAddress)
            intent.putExtra("customerPhoneNumber", customerPhoneNumber)
            intent.putExtra("customerCluster", customerCluster)
            startActivity(intent)
        }
    }
}