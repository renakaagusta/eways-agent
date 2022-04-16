package com.eways.agent.order.activity.getcustomer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.proyek.infrastructures.order.order.network.body.LaporanKerusakanBody
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.user.agent.usecases.GetAgentDetail
import com.proyek.infrastructures.utils.Authenticated
import kotlinx.android.synthetic.main.activity_getcustomer_pickupticket_form.*

class GetCustomerPickupTicketFormActivity : BaseActivity() {
    private val requestCodeCurrentServicePacket = 1
    private lateinit var currentServicePacketOption : String
    private var pickupTicketDescription : String? =null

    private lateinit var internetServiceId: String
    private lateinit var internetService: InternetService
    private lateinit var agent: UserAgent

    private lateinit var getInternetServiceDetail: GetInternetServiceDetail
    private lateinit var getAgentDetail: GetAgentDetail

    private lateinit var customerName: String
    private lateinit var customerAddress: String
    private lateinit var customerPhoneNumber: String
    private lateinit var customerCluster: String

    private lateinit var user: UserAgent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getcustomer_pickupticket_form)
        CustomSupportActionBar.setCustomActionBar(this,"Laporan Kerusakan")

        customerName = intent.getStringExtra("customerName")
        customerAddress = intent.getStringExtra("customerAddress")
        customerPhoneNumber = intent.getStringExtra("customerPhoneNumber")
        customerCluster = intent.getStringExtra("customerCluster")

        getInternetServiceDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetInternetServiceDetail::class.java)
        getAgentDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetAgentDetail::class.java)

        agent = Authenticated.getUserAgent()

        submissionButton()
        formCompleteness()
        setCurrentPacketService()
    }

    private fun isValid():Boolean{
        return tvCurrentServicePacketOption.text.toString() != getString(R.string.currentservicepaket_hint) &&
                Utils.isNotNullOrEmpty(pickupTicketDescription)
    }

    private fun formCompleteness(){
        Utils.setOnTextChanged(tvCurrentServicePacketOption, object : Utils.Companion.OnTextChanged{
            override fun onChange(text: String) {
                currentServicePacketOption = text
            }
        }){this.submissionButton()}
        Utils.setOnTextChanged(etPickupTicketDescription, object : Utils.Companion.OnTextChanged{
            override fun onChange(text: String) {
                pickupTicketDescription = text
            }
        }){this.submissionButton()}
    }

    private fun setCurrentPacketService(){
        rlCurrentServicePacketOption.setOnClickListener {
            val intent = Intent(this, ServicePacketOptionActivity::class.java)
            startActivityForResult(intent,requestCodeCurrentServicePacket)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == requestCodeCurrentServicePacket){

                internetServiceId =  data?.extras?.getSerializable("service_packet") as String

                getInternetServiceDetail.set(internetServiceId, this@GetCustomerPickupTicketFormActivity)
                getInternetServiceDetail.get().observe(this, Observer {
                    internetService = it.data[0]
                    tvCurrentServicePacketOption.text = it.data[0].name
                    tvCurrentServicePacketOption.setTextColor(ContextCompat.getColor(this, R.color.darkText))

                })
            }
        }
    }

    private fun submissionButton(){
        if(!isValid()){
            tvSubmit.isClickable = false
            tvSubmit.background = ContextCompat.getDrawable(this@GetCustomerPickupTicketFormActivity, R.drawable.rc_bglightgray)
        }else{
            tvSubmit.background = ContextCompat.getDrawable(this@GetCustomerPickupTicketFormActivity, R.drawable.rc_bgprimary)
            tvSubmit.isClickable = true
            moveToPickupTicketFormConfirmation()
        }
    }

    private fun moveToPickupTicketFormConfirmation(){
        tvSubmit.setOnClickListener {
            Log.d("desc", etPickupTicketDescription.text.toString())

            Log.d("desc", "etPickupTicketDescription.text.toString()")
            val laporanKerusakanOrderBody =
                LaporanKerusakanBody(
                    internetService,
                    etPickupTicketDescription.text.toString(),
                    "1apPg8GAOei7bEsom7fxB0wBgxD",
                    customerName,
                    customerAddress,
                    customerPhoneNumber,
                    customerCluster,
                    agent.ID!!
                )
            val intent = Intent(this, GetCustomerPickupTicketFormConfirmationActivity::class.java)
            intent.putExtra("body", laporanKerusakanOrderBody)
            intent.putExtra("agent", agent)
            intent.putExtra("customerName", customerName)
            intent.putExtra("customerAddress", customerAddress)
            intent.putExtra("customerPhoneNumber", customerPhoneNumber)
            intent.putExtra("customerCluster", customerCluster)
            startActivity(intent)
        }
    }
}