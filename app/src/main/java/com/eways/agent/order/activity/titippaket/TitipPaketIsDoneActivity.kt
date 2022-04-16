package com.eways.agent.order.activity.titippaket

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.utils.MoneyUtils
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.eways.agent.utils.date.SLDate
import com.google.gson.Gson
import com.proyek.infrastructures.order.order.entities.Orderable
import com.proyek.infrastructures.order.order.usecases.AgentFinishOrder
import com.proyek.infrastructures.order.order.usecases.GetOrderDetail
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.user.cluster.usecases.GetClusterDetail
import com.proyek.infrastructures.user.customer.usecases.GetCustomerDetail
import com.proyek.infrastructures.utils.Authenticated
import kotlinx.android.synthetic.main.activity_titippaket_isdone.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class TitipPaketIsDoneActivity : BaseActivity() {

    private lateinit var user: UserAgent
    private lateinit var getOrderDetail: GetOrderDetail
    private lateinit var getClusterDetail: GetClusterDetail
    private lateinit var getCustomerDetail: GetCustomerDetail
    private lateinit var finishedOrder: AgentFinishOrder
    private lateinit var orderId : String
    private lateinit var imgProfile : ImageView

    private var note: String? = null
    private var review: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_titippaket_isdone)
        CustomSupportActionBar.setCustomActionBar(this, "Detail Riwayat")
    }

    override fun onStart() {
        super.onStart()

        user = Authenticated.getUserAgent()
        orderId = intent.getStringExtra("id")

        getOrderDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetOrderDetail::class.java)
        getClusterDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetClusterDetail::class.java)
        getCustomerDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetCustomerDetail::class.java)
        finishedOrder = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(AgentFinishOrder::class.java)

        imgProfile = findViewById(R.id.imgProfile)

        setData()

    }

    private fun setData(){

        this@TitipPaketIsDoneActivity.showProgress()

        GlobalScope.launch(Dispatchers.Main) {
            lateinit var order: Orderable

            var customerId = ""
            val SLDate = SLDate()
            val gson = Gson()

            getOrderDetail.set(this@TitipPaketIsDoneActivity,orderId)
            delay(500)
            getOrderDetail.get().forEach {
                order = gson.fromJson(it.order, Orderable::class.java)
                SLDate.date = SimpleDateFormat("yyyy-MM-dd").parse(it.createdAt)
                tvDate.text = SLDate.getLocalizeDateString()
                tvAgentFee.text = MoneyUtils.getAmountString(order.service?.agentFee)
                tvTitipPaketFee.text = MoneyUtils.getAmountString(it.orderFee)
                tvTotal.text = MoneyUtils.getAmountString(order.service?.agentFee?.plus(it.orderFee!!))

                tvSenderName.text = order.senderName
                tvSenderPhone.text = order.senderPhoneNumber
                tvSenderAddress.text = order.senderAddress

                tvReceiverName.text = order.receiverName
                tvReceiverAddress.text = order.receiverAddress
                tvReceiverPhone.text = order.receiverPhoneNumber

                tvItemName.text = order.packetName
                tvItemDetail.text = order.packetDescription

                customerId = it.customerId!!

                if(it.agentNote!=null) note = it.agentNote
                if(it.customerReview!=null) review = it.customerReview

                if(note!=null)tvNote.text = note
                if(review!=null)tvReview.text = review
            }

            delay(500)

            if(customerId!=user.ID) {
                getCustomerDetail.set(customerId, this@TitipPaketIsDoneActivity)
                delay(1000)
                getCustomerDetail.get().observe(this@TitipPaketIsDoneActivity, Observer {
                    Log.d("it", it.toString())
                    tvCustomerName.text = it.data[0].username
                    tvCustomerAddress.text = it.data[0].address
                    tvCustomerPhone.text = it.data[0].phoneNumber
                    Log.d("image", it.data[0].imagePath.toString())
                    if (it.data[0].imagePath != null)
                        Glide.with(this@TitipPaketIsDoneActivity)
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

            this@TitipPaketIsDoneActivity.dismissProgress()
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