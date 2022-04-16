package com.eways.agent.order.activity.gantipaket

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.dashboard.activity.MainActivity
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.eways.agent.utils.date.SLDate
import com.google.gson.Gson
import com.proyek.infrastructures.notification.usecases.CreateOrderNotification
import com.proyek.infrastructures.order.order.entities.Orderable
import com.proyek.infrastructures.order.order.usecases.AcceptOrder
import com.proyek.infrastructures.order.order.usecases.GetOrderDetail
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.user.cluster.usecases.GetClusterDetail
import com.proyek.infrastructures.user.customer.usecases.GetCustomerDetail
import kotlinx.android.synthetic.main.activity_gantipaket_onrequest.*
import kotlinx.android.synthetic.main.activity_gantipaket_onrequest.tvAccept
import kotlinx.android.synthetic.main.activity_gantipaket_onrequest.tvCustomerAddress
import kotlinx.android.synthetic.main.activity_gantipaket_onrequest.tvCustomerCluster
import kotlinx.android.synthetic.main.activity_gantipaket_onrequest.tvCustomerName
import kotlinx.android.synthetic.main.activity_gantipaket_onrequest.tvCustomerPhone
import kotlinx.android.synthetic.main.activity_gantipaket_onrequest.tvDate
import kotlinx.android.synthetic.main.activity_gantipaket_onrequest.tvServicePacketName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class GantiPaketOnRequestActivity : BaseActivity(){
    private lateinit var user: UserAgent
    private lateinit var getOrderDetail: GetOrderDetail
    private lateinit var getClusterDetail: GetClusterDetail
    private lateinit var getCustomerDetail: GetCustomerDetail
    private lateinit var createNotification: CreateOrderNotification

    private lateinit var acceptOrder: AcceptOrder
    private lateinit var orderId : String
    private var customerId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gantipaket_onrequest)
        CustomSupportActionBar.setCustomActionBar(this, "Detail Pesanan")
    }

    override fun onStart() {
        super.onStart()

        user = intent.getParcelableExtra("user")
        orderId = intent.getStringExtra("id")

        getOrderDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetOrderDetail::class.java)
        getClusterDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            GetClusterDetail::class.java)
        getCustomerDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetCustomerDetail::class.java)
        acceptOrder = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(AcceptOrder::class.java)
        createNotification = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(CreateOrderNotification::class.java)

        setData(this)
        acceptOrder()

    }

    private fun setData(lifecycleOwner: LifecycleOwner){

        this@GantiPaketOnRequestActivity.showProgress()

        GlobalScope.launch(Dispatchers.Main) {
            val SLDate = SLDate()

            val gson = Gson()

            getOrderDetail.set(this@GantiPaketOnRequestActivity,orderId)
            delay(500)
            getOrderDetail.get().forEach {
                val order = gson.fromJson(it.order, Orderable::class.java)
                tvServicePacketName.text = order.newInternetService.name
                tvCurrentServicePacketName.text = order.oldInternetService.name
                SLDate.date = SimpleDateFormat("yyyy-MM-dd").parse(it.createdAt)
                tvDate.text = SLDate.getLocalizeDateString()
                customerId = it.customerId!!
            }

            delay(500)

            getCustomerDetail.set(customerId, this@GantiPaketOnRequestActivity)
            delay(1000)
            getCustomerDetail.get().observe(lifecycleOwner, Observer {
                tvCustomerName.text = it.data[0].username
                tvCustomerAddress.text = it.data[0].address
                tvCustomerPhone.text = it.data[0].phoneNumber
                if(it.data[0].imagePath!=null)
                    Glide.with(this@GantiPaketOnRequestActivity)
                        .load("http://13.229.200.77:8001/storage/${it.data[0].imagePath}")
                        .into(imgProfile)
                if(it.data[0].cluster!=null)
                    tvCustomerCluster.text = it.data[0].cluster?.name!!
                else
                    tvCustomerCluster.text = "-"

            })

            delay(500)

            this@GantiPaketOnRequestActivity.dismissProgress()

        }
    }

    fun moveToMainActivity() {
        val intent = Intent(this@GantiPaketOnRequestActivity, MainActivity::class.java)
        intent.putExtra("agent", user)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun acceptOrder(){
        tvAccept.setOnClickListener {
            this@GantiPaketOnRequestActivity.showProgress()
            acceptOrder.set(orderId, this@GantiPaketOnRequestActivity)
            acceptOrder.get().observe(this, Observer {
                if(it.errors.message?.isEmpty()!!) {
                    GlobalScope.launch(Dispatchers.Main) {
                        createNotification.set(
                            customerId,
                            orderId,
                            user.username!!,
                            "Order Ganti Paket telah dikonfirmasi oleh Agen",
                            this@GantiPaketOnRequestActivity
                        )
                        delay(300)
                        createNotification.set(
                            user.ID!!,
                            orderId,
                            "Eways",
                            "Order dikonfirmasi",
                            this@GantiPaketOnRequestActivity
                        )
                        delay(300)
                        this@GantiPaketOnRequestActivity.dismissProgress()
                        showSuccess("Order berhasil dikonfirmasi")
                        setConfirm(this@GantiPaketOnRequestActivity::moveToMainActivity)
                    }
                } else {
                    this@GantiPaketOnRequestActivity.dismissProgress()
                    this@GantiPaketOnRequestActivity.showError(it.errors.message!![0])
                }
            })
        }
    }
}