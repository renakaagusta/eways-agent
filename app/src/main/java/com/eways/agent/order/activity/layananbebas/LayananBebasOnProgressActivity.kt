package com.eways.agent.order.activity.layananbebas

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
import com.proyek.infrastructures.order.order.usecases.AgentFinishOrder
import com.proyek.infrastructures.order.order.usecases.ConfirmPayment
import com.proyek.infrastructures.order.order.usecases.GetOrderDetail
import com.proyek.infrastructures.order.order.usecases.UpdateAgentNote
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.user.cluster.usecases.GetClusterDetail
import com.proyek.infrastructures.user.customer.usecases.GetCustomerDetail
import kotlinx.android.synthetic.main.activity_layananbebas_onprogress.*
import kotlinx.android.synthetic.main.activity_layananbebas_onprogress.etNote
import kotlinx.android.synthetic.main.activity_layananbebas_onprogress.llChat
import kotlinx.android.synthetic.main.activity_layananbebas_onprogress.tvCustomerAddress
import kotlinx.android.synthetic.main.activity_layananbebas_onprogress.tvCustomerCluster
import kotlinx.android.synthetic.main.activity_layananbebas_onprogress.tvCustomerName
import kotlinx.android.synthetic.main.activity_layananbebas_onprogress.tvCustomerPhone
import kotlinx.android.synthetic.main.activity_layananbebas_onprogress.tvDate
import kotlinx.android.synthetic.main.activity_layananbebas_onprogress.tvDone
import kotlinx.android.synthetic.main.activity_layananbebas_onprogress.tvInput
import kotlinx.android.synthetic.main.activity_layananbebas_onprogress.tvNote
import kotlinx.android.synthetic.main.activity_layananbebas_onprogress.tvPaymentConfirmation
import kotlinx.android.synthetic.main.activity_layananbebas_onprogress.tvTotal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class LayananBebasOnProgressActivity : BaseActivity() {
    private lateinit var user: UserAgent

    private lateinit var getOrderDetail: GetOrderDetail
    private lateinit var getClusterDetail: GetClusterDetail
    private lateinit var getCustomerDetail: GetCustomerDetail
    private lateinit var finishedOrder: AgentFinishOrder
    private lateinit var createNotification: CreateOrderNotification
    private lateinit var confirmPayment: ConfirmPayment
    private lateinit var updateAgentNote: UpdateAgentNote

    private lateinit var orderId : String
    private lateinit var imgProfile : ImageView

    private var customerId = ""

    private var orderStatus = OrderStatus.OnProgress
    private var paymentStatus = PaymentStatus.Unpaid
    private var note: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layananbebas_onprogress)
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
        updateAgentNote = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UpdateAgentNote::class.java)

        imgProfile = findViewById(R.id.imgProfile)

        setData(this)
        moveToChat()
        confirmPayment()
        finishingOrder()
        updateAgentNote()
    }

    private fun updateAgentNote() {
        tvInput.setOnClickListener {
            this@LayananBebasOnProgressActivity.showProgress()
            updateAgentNote.set(orderId, etNote.text.toString(), this@LayananBebasOnProgressActivity)
            updateAgentNote.get().observe(this, Observer {
                this@LayananBebasOnProgressActivity.dismissProgress()
                if(it.errors.message?.isEmpty()!!) {
                    finish()
                } else {
                    this@LayananBebasOnProgressActivity.showError(it.errors.message!![0])
                }
            })
        }
    }

    private fun setData(lifecycleOwner: LifecycleOwner){
        GlobalScope.launch(Dispatchers.Main) {
            lateinit var order: Orderable
            val SLDate = SLDate()
            val gson = Gson()

            this@LayananBebasOnProgressActivity.showProgress()

            getOrderDetail.set(this@LayananBebasOnProgressActivity, orderId)
            delay(500)
            getOrderDetail.get().forEach {
                order = gson.fromJson(it.order, Orderable::class.java)

                tvLayananBebasName.text = order.name
                tvLayananBebasDetail.text = order.description

                note = it.agentNote
                if(note!=null)tvNote.text = it.agentNote

                SLDate.date = SimpleDateFormat("yyyy-MM-dd").parse(it.createdAt)
                tvDate.text = SLDate.getLocalizeDateString()
                note = it.agentNote

                if(it.orderFee==null){
                    tvLayananBebasTransactionFee.text = "-"
                    tvTotal.text = "-"
                }
                else{
                    tvLayananBebasTransactionFee.text = MoneyUtils.getAmountString(it.orderFee)
                    tvTotal.text = MoneyUtils.getAmountString(it.orderFee)
                }


                customerId = it.customerId!!

                if(it.orderStatus==2) orderStatus = OrderStatus.CustomerFinished
                if(it.paymentStatus==1) paymentStatus = PaymentStatus.Paid
            }

            delay(500)

            if(customerId!=user.ID!!) {
                getCustomerDetail.set(customerId, this@LayananBebasOnProgressActivity)
                delay(1000)
                getCustomerDetail.get().observe(lifecycleOwner, Observer {

                    tvCustomerName.text = it.data[0].username
                    tvCustomerAddress.text = it.data[0].address
                    tvCustomerPhone.text = it.data[0].phoneNumber

                    if (it.data[0].imagePath != null)
                        Glide.with(this@LayananBebasOnProgressActivity)
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

            this@LayananBebasOnProgressActivity.dismissProgress()
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

        if((orderStatus == OrderStatus.OnProgress)&&(paymentStatus==PaymentStatus.Unpaid)){
            tvDone.isVisible = false
            tvPaymentConfirmation.isVisible = true
        }else if((orderStatus == OrderStatus.OnProgress)&&(paymentStatus==PaymentStatus.Paid)){
            tvDone.isVisible = true
            tvPaymentConfirmation.isVisible = false
            tvDone.background = ContextCompat.getDrawable(this@LayananBebasOnProgressActivity, R.drawable.rc_bglightgray)
        }else if(orderStatus == OrderStatus.CustomerFinished){
            tvDone.isVisible = true
            tvPaymentConfirmation.isVisible = false
            tvDone.background = ContextCompat.getDrawable(this@LayananBebasOnProgressActivity, R.drawable.rc_bgprimary)
        }

    }

    private fun moveToChat(){
        llChat.setOnClickListener {
            val intent = Intent(this@LayananBebasOnProgressActivity, ChatActivity::class.java)
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
            this@LayananBebasOnProgressActivity.showProgress()
            confirmPayment.set(orderId, this@LayananBebasOnProgressActivity)
            confirmPayment.get().observe(this, Observer {
                if(it.errors.message?.isEmpty()!!) {
                    GlobalScope.launch(Dispatchers.Main) {
                        createNotification.set(
                            customerId,
                            orderId,
                            "Eways",
                            "Pembayaranmu telah dikonfirmasi agen",
                            this@LayananBebasOnProgressActivity
                        )
                        delay(200)
                        createNotification.set(
                            customerId,
                            orderId,
                            "Eways",
                            "Pembayaran dikonfirmasi",
                            this@LayananBebasOnProgressActivity
                        )
                        delay(300)
                        dismissProgress()
                        finish()
                    }
                } else {
                    this@LayananBebasOnProgressActivity.dismissProgress()
                    this@LayananBebasOnProgressActivity.showWarning(it.errors.message!![0])
                }
            })
        }
    }

    private fun finishingOrder(){
        tvDone.setOnClickListener {
            this@LayananBebasOnProgressActivity.showProgress()
            finishedOrder.set(orderId, this@LayananBebasOnProgressActivity)
            finishedOrder.get().observe(this, Observer {
                if(it.errors.message?.isEmpty()!!) {
                    GlobalScope.launch(Dispatchers.Main) {
                        createNotification.set(
                            customerId,
                            orderId,
                            user.username!!,
                            "Order Laporan Kerusakan mu telah diselesaikan",
                            this@LayananBebasOnProgressActivity
                        )
                        delay(300)
                        createNotification.set(
                            user.ID!!,
                            orderId,
                            "Eways",
                            "Order berhasil diselesaikan",
                            this@LayananBebasOnProgressActivity
                        )
                        this@LayananBebasOnProgressActivity.dismissProgress()
                        val intent =
                            Intent(this@LayananBebasOnProgressActivity, MainActivity::class.java)
                        intent.putExtra("agent", user)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }
                } else {
                    this@LayananBebasOnProgressActivity.dismissProgress()
                    this@LayananBebasOnProgressActivity.showError(it.errors.message!![0])
                }
            })
        }
    }
}