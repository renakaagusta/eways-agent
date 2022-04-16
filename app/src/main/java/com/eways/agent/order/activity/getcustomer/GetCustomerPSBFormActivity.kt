package com.eways.agent.order.activity.getcustomer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.order.activity.getcustomer.option.ServicePacketOptionActivity
import com.eways.agent.utils.Utils
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.proyek.infrastructures.inventory.internetservice.entities.InternetService
import com.proyek.infrastructures.inventory.internetservice.usecases.GetInternetServiceDetail
import com.proyek.infrastructures.order.order.network.body.PSBBody
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.utils.Authenticated
import kotlinx.android.synthetic.main.activity_getcustomer_psb_form.*

class GetCustomerPSBFormActivity : BaseActivity() {
    private val requestCodeServicePacket = 1
    private val requestCodeAgent = 2
    private lateinit var servicePacketOption : String

    private lateinit var internetService: InternetService
    private lateinit var getInternetServiceDetail: GetInternetServiceDetail

    private lateinit var customerName: String
    private lateinit var customerAddress: String
    private lateinit var customerPhoneNumber: String
    private lateinit var customerCluster: String

    private lateinit var user: UserAgent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getcustomer_psb_form)
        CustomSupportActionBar.setCustomActionBar(this, "Pesan Pasang Baru")

        getInternetServiceDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetInternetServiceDetail::class.java)

        customerName = intent.getStringExtra("customerName")
        customerAddress = intent.getStringExtra("customerAddress")
        customerPhoneNumber = intent.getStringExtra("customerPhoneNumber")
        customerCluster = intent.getStringExtra("customerCluster")

        user = Authenticated.getUserAgent()

        submissionButton()
        formCompleteness()
        setServicePacket()
    }

    private fun isValid():Boolean{
        return tvServicePacketOption.text.toString() != getString(R.string.servicepacket_hint)
    }

    private fun formCompleteness(){
        Utils.setOnTextChanged(tvServicePacketOption, object : Utils.Companion.OnTextChanged{
            override fun onChange(text: String) {
                servicePacketOption = text
            }
        }){this.submissionButton()}
    }

    private fun setServicePacket(){
        rlServicePacketOption.setOnClickListener {
            val intent = Intent(this, ServicePacketOptionActivity::class.java)
            startActivityForResult(intent,requestCodeServicePacket)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            val internetServiceId =  data?.extras?.getSerializable("service_packet") as String

            getInternetServiceDetail.set(internetServiceId, this@GetCustomerPSBFormActivity)
            getInternetServiceDetail.get().observe(this, Observer {
                internetService = it.data[0]
                tvServicePacketOption.text = it.data[0].name
                tvServicePacketDescription.text = it.data[0].description
                tvServicePacketOption.setTextColor(ContextCompat.getColor(this, R.color.darkText))
            })
        }
    }

    private fun submissionButton(){
        if(!isValid()){
            tvSubmit.isClickable = false
            tvSubmit.background = ContextCompat.getDrawable(this@GetCustomerPSBFormActivity, R.drawable.rc_bglightgray)
        }else{
            tvSubmit.background = ContextCompat.getDrawable(this@GetCustomerPSBFormActivity, R.drawable.rc_bgprimary)
            tvSubmit.isClickable = true
            moveToPSBConfirmation()
        }
    }

    private fun moveToPSBConfirmation(){
        tvSubmit.setOnClickListener {
            val PSBOrder =
                PSBBody(
                    internetService,
                    "1apQwniIJo6WnAm2cIk7WEkzWt0",
                    customerName,
                    customerAddress,
                    customerPhoneNumber,
                    customerCluster,
                    user.ID!!
                )
            val intent = Intent(this, GetCustomerPSBFormConfirmationActivity::class.java)
            intent.putExtra("body", PSBOrder)
            intent.putExtra("agent", user)
            intent.putExtra("customerName", customerName)
            intent.putExtra("customerAddress", customerAddress)
            intent.putExtra("customerPhoneNumber", customerPhoneNumber)
            intent.putExtra("customerCluster", customerCluster)
            startActivity(intent)
        }
    }
}