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
import com.proyek.infrastructures.order.order.network.body.GantiPaketBody
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.utils.Authenticated
import kotlinx.android.synthetic.main.activity_getcustomer_gantipaket_form.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GetCustomerGantiPaketFormActivity : BaseActivity(){
    private val requestCodeServicePacket = 1
    private val requestCodeCurrentServicePacket = 2
    private lateinit var servicePacketOption:String
    private lateinit var currentServicePacketOption:String

    private lateinit var getInternetServiceDetail: GetInternetServiceDetail

    private lateinit var internetServiceId: String
    private lateinit var currentInternetServiceId: String
    private lateinit var agentId: String
    private lateinit var internetService: InternetService
    private lateinit var currentInternetService: InternetService
    private lateinit var agent: UserAgent

    private lateinit var customerName: String
    private lateinit var customerAddress: String
    private lateinit var customerPhoneNumber: String
    private lateinit var customerCluster: String

    private lateinit var user: UserAgent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getcustomer_gantipaket_form)
        CustomSupportActionBar.setCustomActionBar(this,"Pesan Ganti Paket")

        getInternetServiceDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetInternetServiceDetail::class.java)

        customerName = intent.getStringExtra("customerName")
        customerAddress = intent.getStringExtra("customerAddress")
        customerPhoneNumber = intent.getStringExtra("customerPhoneNumber")
        customerCluster = intent.getStringExtra("customerCluster")

        user = Authenticated.getUserAgent()

        submissionButton()
        formCompleteness()
        setCurrentPacketService()
        setServicePacket()
    }


    private fun isValid():Boolean{
        return tvServicePacketOption.text.toString() != getString(R.string.servicepacket_hint) &&
                tvCurrentServicePacketOption.text.toString() != getString(R.string.currentservicepaket_hint)
    }

    private fun formCompleteness(){
        Utils.setOnTextChanged(tvServicePacketOption, object : Utils.Companion.OnTextChanged{
            override fun onChange(text: String) {
                servicePacketOption = text
            }
        }){submissionButton()}
        Utils.setOnTextChanged(tvCurrentServicePacketOption, object : Utils.Companion.OnTextChanged{
            override fun onChange(text: String) {
                currentServicePacketOption = text
            }
        }){submissionButton()}
    }

    private fun setCurrentPacketService(){
        rlCurrentServicePacketOption.setOnClickListener {
            val intent = Intent(this, ServicePacketOptionActivity::class.java)
            startActivityForResult(intent,requestCodeCurrentServicePacket)
        }

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
            if(requestCode == requestCodeServicePacket){
                internetServiceId =  data?.extras?.getSerializable("service_packet") as String

                GlobalScope.launch(Dispatchers.Main) {
                    getInternetServiceDetail.set(internetServiceId, this@GetCustomerGantiPaketFormActivity)
                    getInternetServiceDetail.get().observe(this@GetCustomerGantiPaketFormActivity, Observer {
                        internetService = it.data[0]
                        tvServicePacketOption.text = it.data[0].name
                        tvServicePacketDescription.text = it.data[0].description
                        tvServicePacketOption.setTextColor(
                            ContextCompat.getColor(
                                this@GetCustomerGantiPaketFormActivity,
                                R.color.darkText
                            )
                        )
                    })
                    delay(700)
                    getInternetServiceDetail.get().removeObservers(this@GetCustomerGantiPaketFormActivity)
                }
            }
            if(requestCode == requestCodeCurrentServicePacket){
                currentInternetServiceId =  data?.extras?.getSerializable("service_packet") as String

                GlobalScope.launch(Dispatchers.Main) {
                    getInternetServiceDetail.set(currentInternetServiceId, this@GetCustomerGantiPaketFormActivity)
                    getInternetServiceDetail.get().observe(this@GetCustomerGantiPaketFormActivity, Observer {
                        currentInternetService = it.data[0]
                        tvCurrentServicePacketOption.text = it.data[0].name
                        tvCurrentServicePacketDescription.text = it.data[0].description
                        tvCurrentServicePacketOption.setTextColor(
                            ContextCompat.getColor(
                                this@GetCustomerGantiPaketFormActivity,
                                R.color.darkText
                            )
                        )
                    })
                    delay(700)
                    getInternetServiceDetail.get().removeObservers(this@GetCustomerGantiPaketFormActivity)
                }
            }

        }
    }

    private fun submissionButton(){
        if(!isValid()){
            tvSubmit.isClickable = false
            tvSubmit.background = ContextCompat.getDrawable(this@GetCustomerGantiPaketFormActivity, R.drawable.rc_bglightgray)
        }else{
            tvSubmit.background = ContextCompat.getDrawable(this@GetCustomerGantiPaketFormActivity, R.drawable.rc_bgprimary)
            tvSubmit.isClickable = true
            moveToGantiPaketFormConfirmation()
        }
    }

    private fun moveToGantiPaketFormConfirmation(){
        tvSubmit.setOnClickListener {
            val gantiPaketOrderBody =
                GantiPaketBody(
                    currentInternetService,
                    internetService,
                    "1apRo3tPQuBvIvMsHvJp0VyFBiW",
                    customerName,
                    customerAddress,
                    customerPhoneNumber,
                    customerCluster,
                    user.ID!!
                )
            val intent = Intent(this, GetCustomerGantiPaketFormConfirmationActivity::class.java)
            intent.putExtra("body", gantiPaketOrderBody)
            intent.putExtra("agent", user)
            intent.putExtra("customerName", customerName)
            intent.putExtra("customerAddress", customerAddress)
            intent.putExtra("customerPhoneNumber", customerPhoneNumber)
            intent.putExtra("customerCluster", customerCluster)
            startActivity(intent)
        }
    }
}