package com.eways.agent.order.activity.titippaket

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
import kotlinx.android.synthetic.main.activity_titippaket_onrequest.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class TitipPaketOnRequestActivity : BaseActivity(){

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
        setContentView(R.layout.activity_titippaket_onrequest)
        CustomSupportActionBar.setCustomActionBar(this, "Detail Pesanan")
    }

    override fun onStart() {
        super.onStart()

        user = intent.getParcelableExtra("user")
        orderId = intent.getStringExtra("id")

        getOrderDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetOrderDetail::class.java)
        getClusterDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetClusterDetail::class.java)
        getCustomerDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetCustomerDetail::class.java)
        acceptOrder = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(AcceptOrder::class.java)
        createNotification = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(CreateOrderNotification::class.java)

        setData(this)
        acceptOrder()
    }

    private fun setData(lifecycleOwner: LifecycleOwner){

        this@TitipPaketOnRequestActivity.showProgress()

        GlobalScope.launch(Dispatchers.Main) {
            val SLDate = SLDate()

            val gson = Gson()

            getOrderDetail.set(this@TitipPaketOnRequestActivity,orderId)
            delay(500)
            getOrderDetail.get().forEach {
                Log.d("TitipPaket", it.toString())
                val order = gson.fromJson(it.order, Orderable::class.java)
                SLDate.date = SimpleDateFormat("yyyy-MM-dd").parse(it.createdAt)

                tvSenderName.text = order.senderName
                tvSenderPhone.text = order.senderPhoneNumber
                tvSenderAddress.text = order.senderAddress

                tvReceiverName.text = order.receiverName
                tvReceiverAddress.text = order.receiverAddress
                tvReceiverPhone.text = order.receiverPhoneNumber

                tvItemName.text = order.packetName
                tvItemDetail.text = order.packetDescription

                SLDate.date = SimpleDateFormat("yyyy-MM-dd").parse(it.createdAt)
                tvDate.text = SLDate.getLocalizeDateString()
                customerId = it.customerId!!
            }

            delay(500)

            getCustomerDetail.set(customerId, this@TitipPaketOnRequestActivity)
            delay(1000)
            getCustomerDetail.get().observe(lifecycleOwner, Observer {
                Log.d("responsecustomer", it.toString())
                tvCustomerName.text = it.data[0].username
                tvCustomerAddress.text = it.data[0].address
                tvCustomerPhone.text = it.data[0].phoneNumber
                if(it.data[0].imagePath!=null)
                    Glide.with(this@TitipPaketOnRequestActivity)
                        .load("http://13.229.200.77:8001/storage/${it.data[0].imagePath}")
                        .into(imgProfile)
                tvCustomerCluster.text = it.data[0].cluster?.name!!
            })

            delay(500)

            this@TitipPaketOnRequestActivity.dismissProgress()
        }
    }

    fun moveToMainActivity() {
        val intent = Intent(this@TitipPaketOnRequestActivity, MainActivity::class.java)
        intent.putExtra("agent", user)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun acceptOrder(){
        tvAccept.setOnClickListener {
            this@TitipPaketOnRequestActivity.showProgress()

            acceptOrder.set(orderId, this@TitipPaketOnRequestActivity)
            acceptOrder.get().observe(this, Observer {if(it.errors.message?.isEmpty()!!) {
                GlobalScope.launch(Dispatchers.Main) {
                    createNotification.set(
                        customerId,
                        orderId,
                        user.username!!,
                        "Order Titip Paket mu telah dikonfirmasi oleh Agen",
                        this@TitipPaketOnRequestActivity
                    )
                    delay(500)
                    createNotification.set(
                        user.ID!!,
                        orderId,
                        "Eways",
                        "Order dikonfirmasi",
                        this@TitipPaketOnRequestActivity
                    )
                    delay(500)
                    this@TitipPaketOnRequestActivity.dismissProgress()
                    showSuccess("Order berhasil dikonfirmasi")
                    setConfirm(this@TitipPaketOnRequestActivity::moveToMainActivity)
                }
            } else {
                this@TitipPaketOnRequestActivity.dismissProgress()
                this@TitipPaketOnRequestActivity.showError(it.errors.message!![0])
            }
            })
        }
    }
}