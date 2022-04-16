package com.eways.agent.order.activity.titipbelanja

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.order.adapter.OrderListAdapter
import com.eways.agent.order.const.OrderStatus
import com.eways.agent.order.const.OrderType
import com.eways.agent.order.viewdto.IOrderViewDTO
import com.eways.agent.order.viewdto.OrderBasicViewDTO
import com.eways.agent.utils.customitemdecoration.CustomVerticalItemDecoration
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.eways.agent.utils.date.SLDate
import com.google.gson.Gson
import com.proyek.infrastructures.order.order.entities.Order
import com.proyek.infrastructures.order.order.entities.Orderable
import com.proyek.infrastructures.order.order.usecases.GetAgentCreatedOrder
import com.proyek.infrastructures.user.agent.entities.UserAgent
import kotlinx.android.synthetic.main.activity_loader.*
import java.text.SimpleDateFormat

class TitipBelanjaListActivity : BaseActivity(){

    private lateinit var user: UserAgent
    private lateinit var getAgentCreatedOrder: GetAgentCreatedOrder
    private var countDividerItemDecoration = 0
    private var listOrder = ArrayList<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loader)
        CustomSupportActionBar.setCustomActionBar(this, "Titip Belanja")
        tvActionbarTitle.isVisible = false
    }

    override fun onStart() {
        super.onStart()

        getAgentCreatedOrder = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(GetAgentCreatedOrder::class.java)

        user = intent.getParcelableExtra("user")


        this@TitipBelanjaListActivity.showProgress()

        getAgentCreatedOrder.set(user.ID!!, 6, this@TitipBelanjaListActivity)
        getAgentCreatedOrder.get().observe(this, Observer {
            val listData = ArrayList<OrderBasicViewDTO>()
            listOrder.addAll(it.data)

            it.data.forEach {
                val gson = Gson()
                val order: Orderable = gson.fromJson(it.order, Orderable::class.java)

                val SLDate = SLDate()
                SLDate.date = SimpleDateFormat("yyyy-MM-dd").parse(it.createdAt)

                var orderDescription = ""

                when(order.service?.type){
                    1 -> orderDescription = order.internetService?.name!!
                    2 -> orderDescription = order.damageDescription!!
                    3 -> orderDescription =  "${order.oldInternetService.name}  -  ${order.newInternetService.name}"
                    5 -> orderDescription = order.invoice?.name!!
                    6 -> orderDescription = if(order.groceries[0].items.category?.name!= null) order.groceries[0].items.category?.name!! else ""
                    7 -> orderDescription = order.name!!
                }

                listData.add(
                    OrderBasicViewDTO(
                        it.id!!,
                        orderType(order.service?.type!!)!!,
                        orderDescription,
                        SLDate,
                        orderStatus(it.orderStatus!!)!!
                    )
                )
            }

            listData.reverse()
            setTitipBelanjaData(listData)

            this@TitipBelanjaListActivity.dismissProgress()
        })
    }


    private fun orderStatus(status: Int): OrderStatus? {
        return when (status) {
            1 -> OrderStatus.Created
            2 -> OrderStatus.OnProgress
            3 -> OrderStatus.CustomerFinished
            4 -> OrderStatus.AgentFinished
            else -> OrderStatus.Created
        }
    }

    private fun orderType(type: Int): OrderType? {
        return when (type) {
            1 -> OrderType.PSB
            2 -> OrderType.PickupTicket
            3 -> OrderType.GantiPaket
            4 -> OrderType.TitipPaket
            5 -> OrderType.TitipBelanja
            6 -> OrderType.TitipBelanja
            7 -> OrderType.LayananBebas
            else -> null
        }
    }


    private fun setTitipBelanjaData(listOrderViewDTO: ArrayList<OrderBasicViewDTO>) {
        val listTitipBelanjaViewDTO: MutableList<IOrderViewDTO> = arrayListOf()
        for (orderViewDTO in listOrderViewDTO) {
            if (orderViewDTO.orderType == OrderType.TitipBelanja && orderViewDTO.orderStatus == OrderStatus.Created) {
                listTitipBelanjaViewDTO.add(orderViewDTO)
            }
        }

        if (listTitipBelanjaViewDTO.isEmpty()) {
            apply {
                rvItem.isVisible = false
                tvItemNoData.isVisible = true
                tvItemNoData.text = "Anda tidak memiliki pesanan baru"
            }
        } else {
            tvItemNoData.isVisible = false
            val historyOrderAdapter = OrderListAdapter(listTitipBelanjaViewDTO, user, listOrder)
            rvItem.apply {
                isVisible = true
                layoutManager = LinearLayoutManager(this@TitipBelanjaListActivity)
                if (countDividerItemDecoration == 0) {
                    addItemDecoration(CustomVerticalItemDecoration(15))
                    countDividerItemDecoration += 1
                }
                isNestedScrollingEnabled = false
                adapter = historyOrderAdapter
            }
        }
    }
}