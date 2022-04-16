package com.eways.agent.order.activity

import com.eways.agent.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.eways.agent.core.baseactivity.BaseFragment
import com.eways.agent.order.adapter.OrderListAdapter
import com.eways.agent.order.const.OrderStatus
import com.eways.agent.order.const.OrderType
import com.eways.agent.order.viewdto.IOrderViewDTO
import com.eways.agent.order.viewdto.OrderBasicViewDTO
import com.eways.agent.order.viewdto.OrderPacketViewDTO
import com.eways.agent.utils.customitemdecoration.CustomVerticalItemDecoration
import com.eways.agent.utils.date.SLDate
import com.google.gson.Gson
import com.proyek.infrastructures.order.order.entities.Order
import com.proyek.infrastructures.order.order.entities.Orderable
import com.proyek.infrastructures.order.order.usecases.GetAgentUnfinishedOrder
import com.proyek.infrastructures.user.agent.entities.UserAgent
import kotlinx.android.synthetic.main.activity_loader.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class OrderFragment(user: UserAgent) : BaseFragment() {
    private val user = user
    private lateinit var getAgentUnfinishedOrder: GetAgentUnfinishedOrder
    private lateinit var myView: View
    private lateinit var lifecycleOwner: LifecycleOwner
    private var countDividerItemDecoration = 0
    private val listOrder = ArrayList<Order>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_loader, container, false)
        view.tvActionbarTitle.text = "Pesanan Aktif"

        getAgentUnfinishedOrder =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                GetAgentUnfinishedOrder::class.java
            )

        myView = view
        lifecycleOwner = this

        setData()

        return view
    }

    private fun setData() {
        showProgress()

        GlobalScope.launch(Dispatchers.Main) {
            getAgentUnfinishedOrder.set(user.ID!!, myView.context)
            delay(500)
            getAgentUnfinishedOrder.get().observe(lifecycleOwner, Observer {
                val listData = ArrayList<IOrderViewDTO>()

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

                    if(order.service?.type==4){
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
                    } else {
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
                }

                dismissProgress()
                listData.reverse()
                setActiveOrderData(myView, listData)
            })
        }

    }

    private fun orderStatus(status: Int): OrderStatus? {
        return when(status){
            0-> OrderStatus.Created
            1-> OrderStatus.OnProgress
            2-> OrderStatus.CustomerFinished
            3-> OrderStatus.AgentFinished
            else-> OrderStatus.Created
        }
    }

    private fun orderType(type: Int): OrderType? {
        return when (type) {
            1 -> OrderType.PSB
            2 -> OrderType.PickupTicket
            3 -> OrderType.GantiPaket
            4 -> OrderType.TitipPaket
            5 -> OrderType.SOPP
            6 -> OrderType.TitipBelanja
            7 -> OrderType.LayananBebas
            else -> null
        }
    }

    private fun setActiveOrderData(view: View, listOrderViewDTO: ArrayList<IOrderViewDTO>) {
        val listActiveOrderViewDTO: MutableList<IOrderViewDTO> = arrayListOf()
        for (orderViewDTO in listOrderViewDTO) {
            if ((orderViewDTO.orderStatus == OrderStatus.OnProgress)||(orderViewDTO.orderStatus == OrderStatus.CustomerFinished)) {
                listActiveOrderViewDTO.add(orderViewDTO)
            }
        }

        if (listActiveOrderViewDTO.isEmpty()) {
            view.apply {
                rvItem.isVisible = false
                tvItemNoData.isVisible = true
                tvItemNoData.text = "Anda tidak memiliki pesanan aktif"
            }
        } else {
            view.tvItemNoData.isVisible = false
            val historyOrderAdapter = OrderListAdapter(listActiveOrderViewDTO, user, listOrder)
            view.rvItem.apply {
                isVisible = true
                layoutManager = LinearLayoutManager(activity)
                if(countDividerItemDecoration==0){
                    addItemDecoration(CustomVerticalItemDecoration(15))
                    countDividerItemDecoration+=1
                }
                isNestedScrollingEnabled = false
                adapter = historyOrderAdapter
            }
        }
    }
}