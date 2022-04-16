package com.eways.agent.order.activity.titipbelanja

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.dashboard.activity.MainActivity
import com.eways.agent.order.activity.chat.ChatActivity
import com.eways.agent.order.adapter.TitipBelanjaTransactionAdapter
import com.eways.agent.order.const.OrderStatus
import com.eways.agent.order.const.PaymentStatus
import com.eways.agent.order.viewdto.TransactionViewDTO
import com.eways.agent.utils.MoneyUtils
import com.eways.agent.utils.customitemdecoration.CustomDividerItemDecoration
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.eways.agent.utils.date.SLDate
import com.google.gson.Gson
import com.proyek.infrastructures.inventory.item.usecases.GetItemDetail
import com.proyek.infrastructures.notification.usecases.CreateOrderNotification
import com.proyek.infrastructures.order.order.entities.Orderable
import com.proyek.infrastructures.order.order.usecases.AgentFinishOrder
import com.proyek.infrastructures.order.order.usecases.ConfirmPayment
import com.proyek.infrastructures.order.order.usecases.GetOrderDetail
import com.proyek.infrastructures.order.order.usecases.UpdateAgentNote
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.user.cluster.usecases.GetClusterDetail
import com.proyek.infrastructures.user.customer.usecases.GetCustomerDetail
import kotlinx.android.synthetic.main.activity_titipbelanja_onprogress.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class TitipBelanjaOnProgressActivity : BaseActivity() {
    private lateinit var user: UserAgent

    private lateinit var getOrderDetail: GetOrderDetail
    private lateinit var getClusterDetail: GetClusterDetail
    private lateinit var getCustomerDetail: GetCustomerDetail
    private lateinit var finishedOrder: AgentFinishOrder
    private lateinit var createNotification: CreateOrderNotification
    private lateinit var confirmPayment: ConfirmPayment
    private lateinit var updateAgentNote: UpdateAgentNote
    private lateinit var getItemDetail: GetItemDetail

    private lateinit var orderId : String
    private lateinit var imgProfile : ImageView
    private var paymentStatus = PaymentStatus.Unpaid
    private var orderStatus = OrderStatus.OnProgress
    private var note: String? = null

    private var customerId = ""
    private var clusterId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_titipbelanja_onprogress)
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
        getItemDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetItemDetail::class.java)

        imgProfile = findViewById(R.id.imgProfile)

        setData()
        moveToChat()
        confirmPayment()
        finishingOrder()
        updateAgentNote()

    }

    private fun updateAgentNote() {
        tvInput.setOnClickListener {
            this@TitipBelanjaOnProgressActivity.showProgress()
            updateAgentNote.set(orderId, etNote.text.toString(), this@TitipBelanjaOnProgressActivity)
            updateAgentNote.get().observe(this, Observer {
                this@TitipBelanjaOnProgressActivity.dismissProgress()
                if(it.errors.message?.isEmpty()!!) {
                    finish()
                } else {
                    this@TitipBelanjaOnProgressActivity.showError(it.errors.message!![0])
                }
            })
        }
    }

    private fun setData(){
        this@TitipBelanjaOnProgressActivity.showProgress()

        GlobalScope.launch(Dispatchers.Main) {
            val listTransactionViewDTO = ArrayList<TransactionViewDTO>()
            var itemId = ""
            lateinit var order: Orderable

            listTransactionViewDTO.clear()

            getOrderDetail.set(this@TitipBelanjaOnProgressActivity, orderId)
            delay(500)
            getOrderDetail.get().forEach {
                val gson = Gson()
                val SLDate = SLDate()

                order = gson.fromJson(it.order, Orderable::class.java)

                note = it.agentNote
                if(note!=null)tvNote.text = it.agentNote

                order.groceries.forEach {
                    if(it.quantity>0) {
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
                }

                tvProductName.text = "Pesan Produk " + order.groceries[0].items.category?.name

                SLDate.date = SimpleDateFormat("yyyy-MM-dd").parse(it.createdAt)

                tvAgentFee.text = MoneyUtils.getAmountString(order.service?.agentFee)

                tvProductName.text = "Pesan produk "+order.groceries[0].items.category?.name

                customerId = it.customerId!!

                if(it.orderStatus==2) orderStatus = OrderStatus.CustomerFinished
                if(it.paymentStatus==1) paymentStatus = PaymentStatus.Paid
            }

            getOrderDetail.result.clear()
            
            delay(500)

            if(customerId!=user.ID) {
                getCustomerDetail.set(customerId, this@TitipBelanjaOnProgressActivity)
                delay(1000)
                getCustomerDetail.get().observe(this@TitipBelanjaOnProgressActivity, Observer {
                    tvCustomerName.text = it.data[0].username
                    tvCustomerAddress.text = it.data[0].address
                    tvCustomerPhone.text = it.data[0].phoneNumber
                    if (it.data[0].imagePath != null)
                        Glide.with(this@TitipBelanjaOnProgressActivity)
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

            this@TitipBelanjaOnProgressActivity.dismissProgress()

            val titipBelanjaTransactionAdapter =
                TitipBelanjaTransactionAdapter(listTransactionViewDTO)
            rvTransaction.apply {
                layoutManager = LinearLayoutManager(this@TitipBelanjaOnProgressActivity)
                addItemDecoration(
                    CustomDividerItemDecoration(
                        ContextCompat.getDrawable(
                            this@TitipBelanjaOnProgressActivity,
                            R.drawable.divider_line
                        )!!
                    )
                )
                isNestedScrollingEnabled = false
                adapter = titipBelanjaTransactionAdapter
            }

            var totalPrice = 0
            for (i in 0 until listTransactionViewDTO.size) {
                totalPrice += listTransactionViewDTO[i].subproductPrice * listTransactionViewDTO[i].suProductAmount
            }

            Log.d("listTransaction", listTransactionViewDTO.toString())
            Log.d("size", listTransactionViewDTO.size.toString())
            Log.d("totalPrice", totalPrice.toString())

            tvTotalBelanja.text = MoneyUtils.getAmountString(totalPrice)

            tvTotalTransaction.text = MoneyUtils.getAmountString(totalPrice + order.service?.agentFee!!)

            setLayout()
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
            tvDone.background = ContextCompat.getDrawable(this@TitipBelanjaOnProgressActivity, R.drawable.rc_bglightgray)
        }else if(orderStatus == OrderStatus.CustomerFinished){
            tvDone.isVisible = true
            tvPaymentConfirmation.isVisible = false
            tvDone.background = ContextCompat.getDrawable(this@TitipBelanjaOnProgressActivity, R.drawable.rc_bgprimary)
        }
    }

    private fun moveToChat(){
        llChat.setOnClickListener {
            val intent = Intent(this@TitipBelanjaOnProgressActivity, ChatActivity::class.java)
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
            this@TitipBelanjaOnProgressActivity.showProgress()
            confirmPayment.set(orderId, this@TitipBelanjaOnProgressActivity)
            confirmPayment.get().observe(this, Observer {
                if(it.errors.message?.isEmpty()!!) {
                    GlobalScope.launch(Dispatchers.Main) {
                        createNotification.set(
                            customerId,
                            orderId,
                            "Eways",
                            "Pembayaranmu telah dikonfirmasi agen",
                            this@TitipBelanjaOnProgressActivity
                        )
                        delay(200)
                        createNotification.set(
                            customerId,
                            orderId,
                            "Eways",
                            "Pembayaran dikonfirmasi",
                            this@TitipBelanjaOnProgressActivity
                        )
                        delay(300)
                        dismissProgress()
                        finish()
                    }
                } else {
                    this@TitipBelanjaOnProgressActivity.dismissProgress()
                    this@TitipBelanjaOnProgressActivity.showError(it.errors.message!![0])
                }
            })
        }
    }

    private fun finishingOrder(){
        tvDone.setOnClickListener {
            this@TitipBelanjaOnProgressActivity.showProgress()
            finishedOrder.set(orderId, this@TitipBelanjaOnProgressActivity)
            finishedOrder.get().observe(this, Observer {
                if(it.errors.message?.isEmpty()!!) {
                    GlobalScope.launch(Dispatchers.Main) {
                        createNotification.set(
                            customerId,
                            orderId,
                            user.username!!,
                            "Order Titip Belanja mu telah diselesaikan",
                            this@TitipBelanjaOnProgressActivity
                        )
                        delay(300)
                        createNotification.set(
                            user.ID!!,
                            orderId,
                            "Eways",
                            "Order berhasil diselesaikan",
                            this@TitipBelanjaOnProgressActivity
                        )
                        this@TitipBelanjaOnProgressActivity.dismissProgress()
                        val intent = Intent(this@TitipBelanjaOnProgressActivity, MainActivity::class.java)
                        intent.putExtra("agent", user)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }
                } else {
                    this@TitipBelanjaOnProgressActivity.dismissProgress()
                    this@TitipBelanjaOnProgressActivity.showError(it.errors.message!![0])
                }
            })
        }
    }
}