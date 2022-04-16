package com.eways.agent.order.activity.titippaket

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
import com.eways.agent.order.viewdto.OrderPacketViewDTO
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

class TitipPaketListActivity : BaseActivity() {

    private lateinit var user: UserAgent
    private lateinit var getAgentCreatedOrder: GetAgentCreatedOrder
    private var countDividerItemDecoration = 0
    private var listOrder = ArrayList<Order>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loader)
        CustomSupportActionBar.setCustomActionBar(this, "Titip Paket")
        tvActionbarTitle.isVisible = false
    }

    override fun onStart() {
        super.onStart()

        getAgentCreatedOrder = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(GetAgentCreatedOrder::class.java)

        user = intent.getParcelableExtra("user")

        this@TitipPaketListActivity.showProgress()

        getAgentCreatedOrder.set(user.ID!!, 4, this@TitipPaketListActivity)
        getAgentCreatedOrder.get().observe(this, Observer {
            val listData = ArrayList<IOrderViewDTO>()
            listOrder.addAll(it.data)

            it.data.forEach {
                val gson = Gson()
                val order: Orderable = gson.fromJson(it.order, Orderable::class.java)

                val SLDate = SLDate()
                SLDate.date = SimpleDateFormat("yyyy-MM-dd").parse(it.createdAt)

                listData.add(
                    OrderPacketViewDTO(
                        it.id!!,
                        orderType(order.service?.type!!)!!,
                        order.senderName!!,
                        order.senderAddress!!,
                        order.receiverName!!,
                        order.receiverAddress!!,
                        SLDate,
                        orderStatus(it.orderStatus!!)!!
                    )
                )
            }

            listData.reverse()
            setTitipPaketData(listData)

            this@TitipPaketListActivity.dismissProgress()
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
            5 -> OrderType.TitipPaket
            6 -> OrderType.TitipPaket
            7 -> OrderType.LayananBebas
            else -> null
        }
    }


    private fun setTitipPaketData(listOrderViewDTO: ArrayList<IOrderViewDTO>) {
        val listTitipPaketViewDTO: MutableList<IOrderViewDTO> = arrayListOf()
        for (orderViewDTO in listOrderViewDTO) {
            if (orderViewDTO.orderType == OrderType.TitipPaket && orderViewDTO.orderStatus == OrderStatus.Created) {
                listTitipPaketViewDTO.add(orderViewDTO)
            }
        }

        if (listTitipPaketViewDTO.isEmpty()) {
            apply {
                rvItem.isVisible = false
                tvItemNoData.isVisible = true
                tvItemNoData.text = "Anda tidak memiliki pesanan baru"
            }
        } else {
            tvItemNoData.isVisible = false
            val historyOrderAdapter = OrderListAdapter(listTitipPaketViewDTO, user, listOrder)
            rvItem.apply {
                isVisible = true
                layoutManager = LinearLayoutManager(this@TitipPaketListActivity)
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