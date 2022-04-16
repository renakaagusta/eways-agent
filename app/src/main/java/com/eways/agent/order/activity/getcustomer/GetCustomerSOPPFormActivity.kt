package com.eways.agent.order.activity.getcustomer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.order.activity.getcustomer.option.SOPPOptionActivity
import com.eways.agent.utils.Utils
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.proyek.infrastructures.inventory.invoice.entities.Invoice
import com.proyek.infrastructures.inventory.invoice.usecases.GetInvoiceDetail
import com.proyek.infrastructures.order.order.network.body.SOPPBody
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.utils.Authenticated
import kotlinx.android.synthetic.main.activity_getcustomer_sopp_form.*

class GetCustomerSOPPFormActivity :BaseActivity(){
    private val requestCodeSOPP = 1
    private lateinit var soppOption : String

    private lateinit var getInvoiceDetail: GetInvoiceDetail

    private lateinit var agent: UserAgent
    private lateinit var invoice: Invoice

    private lateinit var customerName: String
    private lateinit var customerAddress: String
    private lateinit var customerPhoneNumber: String
    private lateinit var customerCluster: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getcustomer_sopp_form)
        CustomSupportActionBar.setCustomActionBar(this,"SOPP")

        getInvoiceDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetInvoiceDetail::class.java)

        customerName = intent.getStringExtra("customerName")
        customerAddress = intent.getStringExtra("customerAddress")
        customerPhoneNumber = intent.getStringExtra("customerPhoneNumber")
        customerCluster = intent.getStringExtra("customerCluster")

        agent = Authenticated.getUserAgent()

        submissionButton()
        formCompleteness()
        setSOPPOption()
    }

    private fun isValid():Boolean{
        return tvSOPPOption.text.toString() != getString(R.string.sopp_hint)
    }

    private fun formCompleteness(){
        Utils.setOnTextChanged(tvSOPPOption, object : Utils.Companion.OnTextChanged{
            override fun onChange(text: String) {
                soppOption = text
            }
        }){this.submissionButton()}
    }

    private fun setSOPPOption(){
        rlSOPPOption.setOnClickListener {
            val intent = Intent(this, SOPPOptionActivity::class.java)
            startActivityForResult(intent, requestCodeSOPP)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if (requestCode == requestCodeSOPP){
                val invoiceId =  data?.extras?.getSerializable("invoice") as String

                this@GetCustomerSOPPFormActivity.showProgress()
                getInvoiceDetail.set(invoiceId, this@GetCustomerSOPPFormActivity)
                getInvoiceDetail.get().observe(this, Observer {
                    this@GetCustomerSOPPFormActivity.dismissProgress()
                    tvSOPPOption.text = it.data[0].name
                    tvSOPPOption.setTextColor(ContextCompat.getColor(this, R.color.darkText))
                    invoice = it.data[0]
                })
            }
        }
    }

    private fun submissionButton(){
        if(!isValid()){
            tvSubmit.isClickable = false
            tvSubmit.background = ContextCompat.getDrawable(this@GetCustomerSOPPFormActivity, R.drawable.rc_bglightgray)
        }else{
            tvSubmit.background = ContextCompat.getDrawable(this@GetCustomerSOPPFormActivity, R.drawable.rc_bgprimary)
            tvSubmit.isClickable = true
            moveToSOPPConfirmation()
        }
    }

    private fun moveToSOPPConfirmation(){
        tvSubmit.setOnClickListener {
            val soppBody = SOPPBody(
                invoice,
                customerName,
                customerPhoneNumber,
                customerAddress,
                etAdditionalInfo.text.toString(),
                "1apSlkvfNVxl5pp6bnzy1RUcZZh",
                customerName,
                customerAddress,
                customerPhoneNumber,
                customerCluster,
                agent.ID!!)
            val intent = Intent(this, GetCustomerSOPPFormConfirmationActivity::class.java)
            intent.putExtra("agent", agent)
            intent.putExtra("body", soppBody)
            intent.putExtra("customerName", customerName)
            intent.putExtra("customerAddress", customerAddress)
            intent.putExtra("customerPhoneNumber", customerPhoneNumber)
            intent.putExtra("customerCluster", customerCluster)
            startActivity(intent)
        }
    }
}