package com.eways.agent.order.activity.getcustomer

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.dashboard.activity.MainActivity
import com.eways.agent.order.adapter.TitipBelanjaTransactionAdapter
import com.eways.agent.order.viewdto.TransactionViewDTO
import com.eways.agent.utils.MoneyUtils
import com.eways.agent.utils.customitemdecoration.CustomDividerItemDecoration
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.proyek.infrastructures.inventory.category.entities.Category
import com.proyek.infrastructures.inventory.item.entities.Grocery
import com.proyek.infrastructures.inventory.service.usecases.GetServiceDetail
import com.proyek.infrastructures.order.order.network.body.TitipBelanjaBody
import com.proyek.infrastructures.order.order.usecases.CreateTitipBelanjaOrder
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.user.agent.usecases.GetAgentDetail
import kotlinx.android.synthetic.main.activity_getcustomer_titipbelanja_form_confirmation.*

class GetCustomerTitipBelanjaFormConfirmationActivity : BaseActivity(){
    private lateinit var agentOption : String
    private var countDividerItemDecoration =0

    private lateinit var agent: UserAgent
    private lateinit var category: Category
    private lateinit var groceries: ArrayList<Grocery>

    private lateinit var createTitipBelanjaOrder: CreateTitipBelanjaOrder
    private lateinit var getServiceDetail: GetServiceDetail

    private lateinit var customerName: String
    private lateinit var customerAddress: String
    private lateinit var customerPhoneNumber: String
    private lateinit var customerCluster: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getcustomer_titipbelanja_form_confirmation)
        CustomSupportActionBar.setCustomActionBar(this, "Detail Pesanan Titip Belanja")

        category = intent.getParcelableExtra("category")
        customerName = intent.getStringExtra("customerName")
        customerAddress = intent.getStringExtra("customerAddress")
        customerPhoneNumber = intent.getStringExtra("customerPhoneNumber")
        customerCluster = intent.getStringExtra("customerCluster")

        createTitipBelanjaOrder = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(CreateTitipBelanjaOrder::class.java)
        getServiceDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetServiceDetail::class.java)

        groceries = intent.getParcelableArrayListExtra("groceries")!!
        agent = intent.getParcelableExtra("agent")

        setData()
        onCOnfirmation()
    }

    private fun setData(){
        val listTransactionViewDTO = ArrayList<TransactionViewDTO>()

        for(i in 0 until groceries.size)
            groceries[i].items.category = category

        groceries.forEach {
            if(it.quantity>0)
            listTransactionViewDTO.add(
                TransactionViewDTO(
                    it.items.imgPath,
                    it.items.name!!,
                    it.quantity,
                    it.items.price!!
                )
            )
        }

        val titipBelanjaTransactionAdapter = TitipBelanjaTransactionAdapter(listTransactionViewDTO)
        rvTransaction.apply {
            layoutManager = LinearLayoutManager(this@GetCustomerTitipBelanjaFormConfirmationActivity)
            addItemDecoration(CustomDividerItemDecoration(ContextCompat.getDrawable(this@GetCustomerTitipBelanjaFormConfirmationActivity, R.drawable.divider_line)!!))
            isNestedScrollingEnabled = false
            adapter = titipBelanjaTransactionAdapter
        }

        var totalPrice = 0
        for (i in 0 until listTransactionViewDTO.size) {
            totalPrice += listTransactionViewDTO[i].subproductPrice * listTransactionViewDTO[i].suProductAmount
        }

        showProgress()
        getServiceDetail.set("1apNPAk6sYE5SoBxfvKT7tlmlqI")
        getServiceDetail.get().observe(this, Observer {
            dismissProgress()
            tvAgentFee.text = MoneyUtils.getAmountString(it[0].agentFee)
            tvTotalBelanja.text = MoneyUtils.getAmountString(totalPrice)
            tvTotalTransaction.text = (totalPrice+it[0].agentFee!!).toString()
        })

        tvTotalBelanja.text = totalPrice.toString()

        tvTotalTransaction.text = (totalPrice+10000).toString()
    }


    private fun moveToMainActivity() {
        createTitipBelanjaOrder.get().removeObservers(this)
        this@GetCustomerTitipBelanjaFormConfirmationActivity.dismissProgress()
        val intent = Intent(this@GetCustomerTitipBelanjaFormConfirmationActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun onCOnfirmation(){
        tvSubmit.setOnClickListener {
            this@GetCustomerTitipBelanjaFormConfirmationActivity.showProgress()
            val body = TitipBelanjaBody(
                groceries,
                "1apNPAk6sYE5SoBxfvKT7tlmlqI",
                customerName,
                customerAddress,
                customerPhoneNumber,
                customerCluster,
                agent.ID!!)
            createTitipBelanjaOrder.set(body, this@GetCustomerTitipBelanjaFormConfirmationActivity)
            createTitipBelanjaOrder.get().observe(this, Observer{
                this@GetCustomerTitipBelanjaFormConfirmationActivity.dismissProgress()
                if(it.errors.message?.isEmpty()!!) {
                    this@GetCustomerTitipBelanjaFormConfirmationActivity.showSuccess("Order dikonfirmasi")
                    this@GetCustomerTitipBelanjaFormConfirmationActivity.setConfirm(this@GetCustomerTitipBelanjaFormConfirmationActivity::moveToMainActivity)
                } else {
                    this@GetCustomerTitipBelanjaFormConfirmationActivity.showError(it.errors.message!![0])
                }
            })
        }
    }
}
