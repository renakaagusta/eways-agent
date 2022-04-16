package com.eways.agent.order.activity.getcustomer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.order.activity.getcustomer.option.OrderTypeOptionActivity
import com.eways.agent.order.const.OrderType
import com.eways.agent.utils.Utils
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import kotlinx.android.synthetic.main.activity_getcustomer_form.*

class GetCustomerFormActivity : BaseActivity() {
    private val requestCodeOrderType= 1
    private var customerName : String? =null
    private var customerAddress : String? = null
    private var customerPhone : String? =null
    private var customerCluster : String? =null
    private var orderTypeOption : String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getcustomer_form)
        CustomSupportActionBar.setCustomActionBar(this,"Get Customer")

        submissionButton()
        formCompleteness()
        setOrderType()
    }

    private fun isValid():Boolean{
        return Utils.isNotNullOrEmpty(customerName) &&
                Utils.isNotNullOrEmpty(customerAddress) &&
                Utils.isNotNullOrEmpty(customerPhone) &&
                Utils.isNotNullOrEmpty(customerCluster)&&
                tvOrderTypeOption.text.toString() != getString(R.string.get_customer)
    }

    private fun formCompleteness(){
        Utils.setOnTextChanged(etCustomerName, object : Utils.Companion.OnTextChanged{
            override fun onChange(text: String) {
                customerName = text
            }
        }){this.submissionButton()}
        Utils.setOnTextChanged(etCustomerAddress, object : Utils.Companion.OnTextChanged{
            override fun onChange(text: String) {
                customerAddress = text
            }
        }){this.submissionButton()}
        Utils.setOnTextChanged(etCustomerPhone, object : Utils.Companion.OnTextChanged{
            override fun onChange(text: String) {
                customerPhone = text
            }
        }){this.submissionButton()}
        Utils.setOnTextChanged(etCustomerCluster, object : Utils.Companion.OnTextChanged{
            override fun onChange(text: String) {
                customerCluster = text
            }
        }){this.submissionButton()}
        Utils.setOnTextChanged(tvOrderTypeOption, object : Utils.Companion.OnTextChanged{
            override fun onChange(text: String) {
                orderTypeOption = text
            }
        }){this.submissionButton()}
    }
    
    private fun setOrderType(){
        rlOrderTypeOption.setOnClickListener {
            val intent = Intent(this, OrderTypeOptionActivity::class.java)
            startActivityForResult(intent, requestCodeOrderType)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if (requestCode == requestCodeOrderType){
                val result =  data?.extras?.getSerializable("orderType") as String
                tvOrderTypeOption.text = result
                tvOrderTypeOption.setTextColor(ContextCompat.getColor(this, R.color.darkText))
            }
        }
    }

    private fun submissionButton(){
        if(!isValid()){
            tvSubmit.isClickable = false
            tvSubmit.background = ContextCompat.getDrawable(this@GetCustomerFormActivity, R.drawable.rc_bglightgray)
        }else{
            tvSubmit.background = ContextCompat.getDrawable(this@GetCustomerFormActivity, R.drawable.rc_bgprimary)
            tvSubmit.isClickable = true
            moveToOrderForm()
        }
    }

    private fun moveToOrderForm(){
        tvSubmit.setOnClickListener {
            val customerName = etCustomerName.text.toString()
            val customerAddress = etCustomerAddress.text.toString()
            val customerPhoneNumber = etCustomerPhone.text.toString()
            val customerCluster = etCustomerCluster.text.toString()
            when(orderTypeOption){
                OrderType.PSB.value ->{
                    val intent = Intent(this@GetCustomerFormActivity, GetCustomerPSBFormActivity::class.java)
                    intent.putExtra("customerName", customerName)
                    intent.putExtra("customerAddress", customerAddress)
                    intent.putExtra("customerPhoneNumber", customerPhoneNumber)
                    intent.putExtra("customerCluster", customerCluster)
                    startActivity(intent)
                }
                OrderType.PickupTicket.value ->{
                    val intent = Intent(this@GetCustomerFormActivity, GetCustomerPickupTicketFormActivity::class.java)
                    intent.putExtra("customerName", customerName)
                    intent.putExtra("customerAddress", customerAddress)
                    intent.putExtra("customerPhoneNumber", customerPhoneNumber)
                    intent.putExtra("customerCluster", customerCluster)
                    startActivity(intent)
                }
                OrderType.GantiPaket.value ->{
                    val intent = Intent(this@GetCustomerFormActivity, GetCustomerGantiPaketFormActivity::class.java)
                    intent.putExtra("customerName", customerName)
                    intent.putExtra("customerAddress", customerAddress)
                    intent.putExtra("customerPhoneNumber", customerPhoneNumber)
                    intent.putExtra("customerCluster", customerCluster)
                    startActivity(intent)
                }
                OrderType.TitipPaket.value ->{
                    val intent = Intent(this@GetCustomerFormActivity, GetCustomerTitipPaketFormActivity::class.java)
                    intent.putExtra("customerName", customerName)
                    intent.putExtra("customerAddress", customerAddress)
                    intent.putExtra("customerPhoneNumber", customerPhoneNumber)
                    intent.putExtra("customerCluster", customerCluster)
                    startActivity(intent)
                }
                OrderType.TitipBelanja.value ->{
                    val intent = Intent(this@GetCustomerFormActivity, GetCustomerTitipBelanjaFormProductActivity::class.java)
                    intent.putExtra("customerName", customerName)
                    intent.putExtra("customerAddress", customerAddress)
                    intent.putExtra("customerPhoneNumber", customerPhoneNumber)
                    intent.putExtra("customerCluster", customerCluster)
                    startActivity(intent)
                }
                OrderType.SOPP.value ->{
                    val intent = Intent(this@GetCustomerFormActivity, GetCustomerSOPPFormActivity::class.java)
                    intent.putExtra("customerName", customerName)
                    intent.putExtra("customerAddress", customerAddress)
                    intent.putExtra("customerPhoneNumber", customerPhoneNumber)
                    intent.putExtra("customerCluster", customerCluster)
                    startActivity(intent)
                }
                OrderType.LayananBebas.value ->{
                    val intent = Intent(this@GetCustomerFormActivity, GetCustomerLayananBebasFormActivity::class.java)
                    intent.putExtra("customerName", customerName)
                    intent.putExtra("customerAddress", customerAddress)
                    intent.putExtra("customerPhoneNumber", customerPhoneNumber)
                    intent.putExtra("customerCluster", customerCluster)
                    startActivity(intent)
                }
            }
        }
    }

}