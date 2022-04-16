package com.eways.agent.order.activity.titippaket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.dashboard.activity.MainActivity
import com.eways.agent.order.activity.chat.ChatActivity
import com.eways.agent.order.const.OrderStatus
import com.eways.agent.order.const.PaymentStatus
import com.eways.agent.utils.MoneyUtils
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.eways.agent.utils.date.SLDate
import com.google.gson.Gson
import com.proyek.infrastructures.notification.usecases.CreateOrderNotification
import com.proyek.infrastructures.order.order.entities.Orderable
import com.proyek.infrastructures.order.order.usecases.*
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.user.cluster.usecases.GetClusterDetail
import com.proyek.infrastructures.user.customer.usecases.GetCustomerDetail
import kotlinx.android.synthetic.main.activity_titippaket_onprogress.*
import kotlinx.android.synthetic.main.activity_titippaket_onprogress.etNote
import kotlinx.android.synthetic.main.activity_titippaket_onprogress.llChat
import kotlinx.android.synthetic.main.activity_titippaket_onprogress.tvAgentFee
import kotlinx.android.synthetic.main.activity_titippaket_onprogress.tvCustomerAddress
import kotlinx.android.synthetic.main.activity_titippaket_onprogress.tvCustomerCluster
import kotlinx.android.synthetic.main.activity_titippaket_onprogress.tvCustomerName
import kotlinx.android.synthetic.main.activity_titippaket_onprogress.tvCustomerPhone
import kotlinx.android.synthetic.main.activity_titippaket_onprogress.tvDate
import kotlinx.android.synthetic.main.activity_titippaket_onprogress.tvDone
import kotlinx.android.synthetic.main.activity_titippaket_onprogress.tvInput
import kotlinx.android.synthetic.main.activity_titippaket_onprogress.tvNote
import kotlinx.android.synthetic.main.activity_titippaket_onprogress.tvPaymentConfirmation
import kotlinx.android.synthetic.main.activity_titippaket_onprogress.tvTotal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class TitipPaketOnProgressActivity :BaseActivity() {
    private lateinit var user: UserAgent

    private lateinit var getOrderDetail: GetOrderDetail
    private lateinit var getClusterDetail: GetClusterDetail
    private lateinit var getCustomerDetail: GetCustomerDetail
    private lateinit var finishedOrder: AgentFinishOrder
    private lateinit var createNotification: CreateOrderNotification
    private lateinit var confirmPayment: ConfirmPayment
    private lateinit var confirmOrderPackageArrived: ConfirmOrderPackageArrived
    private lateinit var updateAgentNote: UpdateAgentNote

    private lateinit var orderId : String
    private lateinit var imgProfile : ImageView

    private var customerId = ""

    var note: String? = null
    private var arrivalStatus = 0
    private var orderStatus = OrderStatus.OnProgress
    private var paymentStatus= PaymentStatus.Unpaid
    enum class PacketStatus{
        Arrived, OnTheWay
    }
    private var packetStatus = PacketStatus.OnTheWay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_titippaket_onprogress)
        CustomSupportActionBar.setCustomActionBar(this, "Detail Pesanan")
    }

    override fun onStart() {
        super.onStart()

        user = intent.getParcelableExtra("user")
        orderId = intent.getStringExtra("id")

        getOrderDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetOrderDetail::class.java)
        getClusterDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetClusterDetail::class.java)
        getCustomerDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetCustomerDetail::class.java)
        finishedOrder = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(AgentFinishOrder::class.java)
        createNotification = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(CreateOrderNotification::class.java)
        confirmPayment = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ConfirmPayment::class.java)
        confirmOrderPackageArrived = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ConfirmOrderPackageArrived::class.java)
        updateAgentNote = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UpdateAgentNote::class.java)

        imgProfile = findViewById(R.id.imgProfile)

        setData(this)
        confirmPayment()
        confirmArrival()
        updateAgentNote()
        moveToChat()
        finishingOrder()
    }

    private fun setData(lifecycleOwner: LifecycleOwner){
        GlobalScope.launch(Dispatchers.Main) {
            lateinit var order: Orderable
            val SLDate = SLDate()
            val gson = Gson()

            this@TitipPaketOnProgressActivity.showProgress()

            getOrderDetail.set(this@TitipPaketOnProgressActivity,orderId)
            delay(500)
            getOrderDetail.get().forEach {
                order = gson.fromJson(it.order, Orderable::class.java)
                SLDate.date = SimpleDateFormat("yyyy-MM-dd").parse(it.createdAt)

                tvDate.text = SLDate.getLocalizeDateString()

                note = it.agentNote
                if(note!=null)tvNote.text = it.agentNote
                tvAgentFee.text = MoneyUtils.getAmountString(order.service?.agentFee)
                if(it.orderFee!=null) {
                    tvTitipPaketFee.text = MoneyUtils.getAmountString(it.orderFee)
                    tvTotal.text = MoneyUtils.getAmountString(order.service?.agentFee?.plus(it.orderFee!!))
                } else {
                    tvTitipPaketFee.text = "-"
                    tvTotal.text = "-"
                }

                tvSenderName.text = order.senderName
                tvSenderPhone.text = order.senderPhoneNumber
                tvSenderAddress.text = order.senderAddress

                tvReceiverName.text = order.receiverName
                tvReceiverAddress.text = order.receiverAddress
                tvReceiverPhone.text = order.receiverPhoneNumber

                tvItemName.text = order.packetName
                tvItemDetail.text = order.packetDescription

                customerId = it.customerId!!

                if(it.orderStatus==2) orderStatus = OrderStatus.CustomerFinished
                if(it.paymentStatus==1) paymentStatus = PaymentStatus.Paid
                if(it.arrivalStatus==1) packetStatus = PacketStatus.Arrived
            }

            delay(500)

            if(user.ID!=customerId) {
                getCustomerDetail.set(customerId, this@TitipPaketOnProgressActivity)
                delay(1000)
                getCustomerDetail.get().observe(lifecycleOwner, Observer {
                    tvCustomerName.text = it.data[0].username
                    tvCustomerAddress.text = it.data[0].address
                    tvCustomerPhone.text = it.data[0].phoneNumber

                    if (it.data[0].imagePath != null)
                        Glide.with(this@TitipPaketOnProgressActivity)
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

                llChat.isVisible = false
            }

            delay(500)

            setLayout()

            this@TitipPaketOnProgressActivity.dismissProgress()
        }

    }

    private fun updateAgentNote() {
        tvInput.setOnClickListener {
            this@TitipPaketOnProgressActivity.showProgress()
            updateAgentNote.set(orderId, etNote.text.toString(), this@TitipPaketOnProgressActivity)
            updateAgentNote.get().observe(this, Observer {
                this@TitipPaketOnProgressActivity.dismissProgress()
                if(it.errors.message?.isEmpty()!!) {
                    finish()
                } else {
                    this@TitipPaketOnProgressActivity.showError(it.errors.message!![0])
                }
            })
        }
    }

    private fun setLayout(){
        if(note==null){
            etNote.isVisible = true
            tvInput.isVisible = true
            tvNote.isVisible = false
        }else{
            etNote.isVisible = false
            tvInput.isVisible = false
            tvNote.isVisible = true
        }

        if(orderStatus == OrderStatus.OnProgress){
            when(paymentStatus){
                PaymentStatus.Unpaid ->{
                    tvDone.isVisible = false
                    when(packetStatus){
                        PacketStatus.OnTheWay->{
                            tvPaymentConfirmation.isVisible = false
                            tvPacketArrived.isVisible = true
                        }
                        PacketStatus.Arrived->{
                            tvPaymentConfirmation.isVisible = true
                            tvPacketArrived.isVisible = false
                            tvStatus.text = "Paket telah sampai di Elkokar"
                        }
                    }
                }
                PaymentStatus.Paid -> {
                    tvDone.isVisible = true
                    tvPacketArrived.isVisible = false
                    tvPaymentConfirmation.isVisible = false
                }
            }
        }else if(orderStatus == OrderStatus.CustomerFinished){
            tvDone.isVisible = true
            tvPacketArrived.isVisible = false
            tvPaymentConfirmation.isVisible = false
            tvDone.background = ContextCompat.getDrawable(this@TitipPaketOnProgressActivity, R.drawable.rc_bgprimary)
        }
    }


    private fun moveToChat(){
        llChat.setOnClickListener {
            val intent = Intent(this@TitipPaketOnProgressActivity, ChatActivity::class.java)
            intent.putExtra("agentId", user.ID)
            intent.putExtra("agentUserName", user.username)
            intent.putExtra("customerId", customerId)
            intent.putExtra("customerUserName", tvCustomerName.text.toString())
            intent.putExtra("orderId", orderId)
            startActivity(intent)
        }
    }

    private fun confirmPayment() {
        tvPaymentConfirmation.setOnClickListener {
            this@TitipPaketOnProgressActivity.showProgress()
            confirmPayment.set(orderId, this@TitipPaketOnProgressActivity)
            confirmPayment.get().observe(this, Observer {
                if(it.errors.message?.isEmpty()!!) {
                    GlobalScope.launch(Dispatchers.Main) {
                        createNotification.set(
                            customerId,
                            orderId,
                            "Eways",
                            "Pembayaranmu telah dikonfirmasi agen",
                            this@TitipPaketOnProgressActivity
                        )
                        delay(200)
                        createNotification.set(
                            customerId,
                            orderId,
                            "Eways",
                            "Pembayaran dikonfirmasi",
                            this@TitipPaketOnProgressActivity
                        )
                        delay(300)
                        dismissProgress()
                        finish()
                    }
                } else {
                    this@TitipPaketOnProgressActivity.dismissProgress()
                    this@TitipPaketOnProgressActivity.showWarning(it.errors.message!![0])
                }
            })
        }
    }

    private fun confirmArrival() {
        tvPacketArrived.setOnClickListener {
            this@TitipPaketOnProgressActivity.showProgress()
            confirmOrderPackageArrived.set(orderId, this@TitipPaketOnProgressActivity)
            confirmOrderPackageArrived.get().observe(this, Observer {
                this@TitipPaketOnProgressActivity.dismissProgress()
                if (it.errors.message?.isEmpty()!!) {
                    createNotification.set(customerId, orderId, "Paketmu telah sampai di Elkokar", "", this@TitipPaketOnProgressActivity)
                    finish()
                } else {
                    this@TitipPaketOnProgressActivity.showError(it.errors.message!![0])
                }
            })
        }
    }

    private fun finishingOrder(){
        tvDone.setOnClickListener {
            this@TitipPaketOnProgressActivity.showProgress()
            finishedOrder.set(orderId, this@TitipPaketOnProgressActivity)
            finishedOrder.get().observe(this, Observer {
                if(it.errors.message?.isEmpty()!!) {
                    GlobalScope.launch(Dispatchers.Main) {
                        createNotification.set(
                            customerId,
                            orderId,
                            user.username!!,
                            "Order Laporan Kerusakan mu telah diselesaikan",
                            this@TitipPaketOnProgressActivity
                        )
                        delay(300)
                        createNotification.set(
                            user.ID!!,
                            orderId,
                            "Eways",
                            "Order berhasil diselesaikan",
                            this@TitipPaketOnProgressActivity
                        )
                        this@TitipPaketOnProgressActivity.dismissProgress()
                        val intent = Intent(this@TitipPaketOnProgressActivity, MainActivity::class.java)
                        intent.putExtra("agent", user)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }
                } else {
                    this@TitipPaketOnProgressActivity.dismissProgress()
                    this@TitipPaketOnProgressActivity.showError(it.errors.message!![0])
                }
            })
        }
    }
}