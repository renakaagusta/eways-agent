package com.eways.agent.order.activity.titipbelanja

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.dashboard.activity.MainActivity
import com.eways.agent.order.adapter.TitipBelanjaTransactionAdapter
import com.eways.agent.order.viewdto.TransactionViewDTO
import com.eways.agent.utils.customitemdecoration.CustomDividerItemDecoration
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.google.gson.Gson
import com.proyek.infrastructures.inventory.item.usecases.GetItemDetail
import com.proyek.infrastructures.notification.usecases.CreateOrderNotification
import com.proyek.infrastructures.order.order.entities.Orderable
import com.proyek.infrastructures.order.order.usecases.AcceptOrder
import com.proyek.infrastructures.order.order.usecases.GetOrderDetail
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.user.cluster.usecases.GetClusterDetail
import com.proyek.infrastructures.user.customer.usecases.GetCustomerDetail
import kotlinx.android.synthetic.main.activity_psb_onrequest.*
import kotlinx.android.synthetic.main.activity_titipbelanja_onrequest.*
import kotlinx.android.synthetic.main.activity_titipbelanja_onrequest.imgProfile
import kotlinx.android.synthetic.main.activity_titipbelanja_onrequest.tvAccept
import kotlinx.android.synthetic.main.activity_titipbelanja_onrequest.tvCustomerAddress
import kotlinx.android.synthetic.main.activity_titipbelanja_onrequest.tvCustomerCluster
import kotlinx.android.synthetic.main.activity_titipbelanja_onrequest.tvCustomerName
import kotlinx.android.synthetic.main.activity_titipbelanja_onrequest.tvCustomerPhone
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TitipBelanjaOnRequestActivity : BaseActivity() {
    private lateinit var user: UserAgent

    private lateinit var getOrderDetail: GetOrderDetail
    private lateinit var getClusterDetail: GetClusterDetail
    private lateinit var getCustomerDetail: GetCustomerDetail
    private lateinit var createNotification: CreateOrderNotification
    private lateinit var getItemDetail: GetItemDetail

    private lateinit var acceptOrder: AcceptOrder
    private lateinit var orderId : String
    private var customerId = ""
    private var clusterId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_titipbelanja_onrequest)
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
        getItemDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetItemDetail::class.java)

        setData(this)
        acceptOrder()

    }

    private fun setData(lifecycleOwner: LifecycleOwner){
        this@TitipBelanjaOnRequestActivity.showProgress()

        GlobalScope.launch(Dispatchers.Main) {
            val listTransactionViewDTO = ArrayList<TransactionViewDTO>()
            var itemId = ""

            getOrderDetail.set(this@TitipBelanjaOnRequestActivity, orderId)
            delay(500)
            getOrderDetail.get().forEach {
                val gson = Gson()
                val order = gson.fromJson(it.order, Orderable::class.java)

                order.groceries.forEach {
                    if(it.quantity>0) {
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

                tvAgentFee.text = order.service?.agentFee.toString()

                customerId = it.customerId!!
                tvProductName.text = "Pesan Produk " + order.groceries[0].items.category?.name
            }

            delay(500)

            getCustomerDetail.set(customerId, this@TitipBelanjaOnRequestActivity)
            delay(1000)
            getCustomerDetail.get().observe(lifecycleOwner, Observer {
                Log.d("it", it.toString())
                tvCustomerName.text = it.data[0].username
                tvCustomerAddress.text = it.data[0].address
                tvCustomerPhone.text = it.data[0].phoneNumber
                if (it.data[0].imagePath != null)
                    Glide.with(this@TitipBelanjaOnRequestActivity)
                        .load("http://13.229.200.77:8001/storage/${it.data[0].imagePath}")
                        .into(imgProfile)

                if(it.data[0].cluster!=null)
                    tvCustomerCluster.text = it.data[0].cluster?.name!!
                else
                    tvCustomerCluster.text = "-"
            })

            delay(500)

            this@TitipBelanjaOnRequestActivity.dismissProgress()

            val titipBelanjaTransactionAdapter =
                TitipBelanjaTransactionAdapter(listTransactionViewDTO)
            rvTransaction.apply {
                layoutManager = LinearLayoutManager(this@TitipBelanjaOnRequestActivity)
                addItemDecoration(
                    CustomDividerItemDecoration(
                        ContextCompat.getDrawable(
                            this@TitipBelanjaOnRequestActivity,
                            R.drawable.divider_line
                        )!!
                    )
                )
                isNestedScrollingEnabled = false
                adapter = titipBelanjaTransactionAdapter
            }

            tvAgentFee.text = "10000"

            var totalPrice = 0
            for (i in 0 until listTransactionViewDTO.size) {
                totalPrice += listTransactionViewDTO[i].subproductPrice * listTransactionViewDTO[i].suProductAmount
            }

            tvTotalBelanja.text = totalPrice.toString()

            tvTotalTransaction.text = (totalPrice + 10000).toString()
        }
    }

    fun moveToMainActivity() {
        val intent = Intent(this@TitipBelanjaOnRequestActivity, MainActivity::class.java)
        intent.putExtra("agent", user)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun acceptOrder(){
        tvAccept.setOnClickListener {
            this@TitipBelanjaOnRequestActivity.showProgress()
            acceptOrder.set(orderId, this@TitipBelanjaOnRequestActivity)
            acceptOrder.get().observe(this, Observer {
                if(it.errors.message?.isEmpty()!!) {
                    GlobalScope.launch(Dispatchers.Main) {
                        createNotification.set(
                            customerId,
                            orderId,
                            user.username!!,
                            "Order Titip Belanja mu telah dikonfirmasi oleh Agen",
                            this@TitipBelanjaOnRequestActivity
                        )
                        delay(300)
                        createNotification.set(
                            user.ID!!,
                            orderId,
                            "Eways",
                            "Order dikonfirmasi",
                            this@TitipBelanjaOnRequestActivity
                        )
                        delay(300)
                        this@TitipBelanjaOnRequestActivity.dismissProgress()
                        showSuccess("Order berhasil dikonfirmasi")
                        setConfirm(this@TitipBelanjaOnRequestActivity::moveToMainActivity)
                    }
                } else {
                    this@TitipBelanjaOnRequestActivity.dismissProgress()
                    this@TitipBelanjaOnRequestActivity.showError(it.errors.message!![0])
                }
            })
        }
    }
}