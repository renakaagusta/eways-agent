package com.eways.agent.order.activity.titipbelanja

import android.graphics.Typeface
import android.os.Bundle
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.order.adapter.TitipBelanjaTransactionAdapter
import com.eways.agent.order.viewdto.TransactionViewDTO
import com.eways.agent.utils.MoneyUtils
import com.eways.agent.utils.customitemdecoration.CustomDividerItemDecoration
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.google.gson.Gson
import com.proyek.infrastructures.inventory.item.usecases.GetItemDetail
import com.proyek.infrastructures.order.order.entities.Orderable
import com.proyek.infrastructures.order.order.usecases.AgentFinishOrder
import com.proyek.infrastructures.order.order.usecases.GetOrderDetail
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.user.cluster.usecases.GetClusterDetail
import com.proyek.infrastructures.user.customer.usecases.GetCustomerDetail
import com.proyek.infrastructures.utils.Authenticated
import kotlinx.android.synthetic.main.activity_titipbelanja_isdone.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TitipBelanjaIsDoneActivity : BaseActivity() {
    private lateinit var user: UserAgent
    private lateinit var getOrderDetail: GetOrderDetail
    private lateinit var getClusterDetail: GetClusterDetail
    private lateinit var getCustomerDetail: GetCustomerDetail
    private lateinit var finishedOrder: AgentFinishOrder
    private lateinit var getItemDetail: GetItemDetail

    private lateinit var orderId : String
    private lateinit var imgProfile : ImageView

    private var note: String? = null
    private var review: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_titipbelanja_isdone)
        CustomSupportActionBar.setCustomActionBar(this,"Detail Riwayat")
    }

    override fun onStart() {
        super.onStart()

        user = Authenticated.getUserAgent()
        orderId = intent.getStringExtra("id")

        getOrderDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetOrderDetail::class.java)
        getClusterDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetClusterDetail::class.java)
        getCustomerDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetCustomerDetail::class.java)
        finishedOrder = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(AgentFinishOrder::class.java)
        getItemDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetItemDetail::class.java)

        imgProfile = findViewById(R.id.imgProfile)

        setData(this)
    }

    private fun setData(lifecycleOwner: LifecycleOwner){

        this@TitipBelanjaIsDoneActivity.showProgress()

        GlobalScope.launch(Dispatchers.Main) {
            val listTransactionViewDTO = ArrayList<TransactionViewDTO>()
            lateinit var order: Orderable
            var customerId = ""
            var itemId = ""


            getOrderDetail.set(this@TitipBelanjaIsDoneActivity,orderId)
            delay(500)
            getOrderDetail.get().forEach {
                val gson = Gson()
                order = gson.fromJson(it.order, Orderable::class.java)

                order.groceries.forEach {
                    itemId = it.items.id!!
                    listTransactionViewDTO.add(
                        TransactionViewDTO(
                            it.items.imgPath,
                            it.items.name!!,
                            it.quantity,
                            it.items.price!!
                        )
                    )
                }

                tvAgentFee.text = MoneyUtils.getAmountString(order.service?.agentFee)
                customerId = it.customerId!!

                tvProductName.text = "Pesan Produk " + order.groceries[0].items.category?.name

                if(it.agentNote!=null) note = it.agentNote
                if(it.customerReview!=null) review = it.customerReview

                if(note!=null)tvNote.text = note
                if(review!=null)tvReview.text = review
            }

            getOrderDetail.get().clear()

            delay(500)

            if(customerId!=user.ID) {
                getCustomerDetail.set(customerId, this@TitipBelanjaIsDoneActivity)
                delay(1000)
                getCustomerDetail.get().observe(lifecycleOwner, Observer {
                    tvCustomerName.text = it.data[0].username
                    tvCustomerAddress.text = it.data[0].address
                    tvCustomerPhone.text = it.data[0].phoneNumber
                    if (it.data[0].imagePath != null)
                        Glide.with(this@TitipBelanjaIsDoneActivity)
                            .load("http://13.229.200.77:8001/storage/${it.data[0].imagePath}")
                            .into(imgProfile)

                    if(it.data[0].cluster!=null)
                        tvCustomerCluster.text = it.data[0].cluster?.name!!
                    else
                        tvCustomerCluster.text = "-"
                })
            } else {
                tvCustomerName.text = order.customer?.name
                tvCustomerAddress.text = order.customer?.address
                tvCustomerPhone.text = order.customer?.phoneNumber
                tvCustomerCluster.text = order.customer?.cluster
            }


            delay(500)

            val titipBelanjaTransactionAdapter =
                TitipBelanjaTransactionAdapter(listTransactionViewDTO)
            rvTransaction.apply {
                layoutManager = LinearLayoutManager(this@TitipBelanjaIsDoneActivity)
                addItemDecoration(
                    CustomDividerItemDecoration(
                        ContextCompat.getDrawable(
                            this@TitipBelanjaIsDoneActivity,
                            R.drawable.divider_line
                        )!!
                    )
                )
                isNestedScrollingEnabled = false
                adapter = titipBelanjaTransactionAdapter
            }

            tvAgentFee.text = order.service?.agentFee.toString()

            var totalPrice = 0
            for (i in 0 until listTransactionViewDTO.size) {
                totalPrice += listTransactionViewDTO[i].subproductPrice * listTransactionViewDTO[i].suProductAmount
            }

            tvTotalBelanja.text = MoneyUtils.getAmountString(totalPrice)

            tvTotalTransaction.text = MoneyUtils.getAmountString(totalPrice + order.service?.agentFee!!)
            delay(500)

            this@TitipBelanjaIsDoneActivity.dismissProgress()
        }
        val typeface : Typeface? = ResourcesCompat.getFont(this.applicationContext, R.font.raleway_mediumitalic)
        if(note!=null){
            tvNote.text = "Isi catatan gaen"
        }else{
            tvNote.text = "Agen tidak memberi catatan apapun"
            tvNote.typeface = typeface

        }

        if(review!=null){
            tvReview.text = "Isi review Pelanggan"
        }else{
            tvReview.text = "Customer belum memberi review"
            tvReview.typeface = typeface
        }
    }
}