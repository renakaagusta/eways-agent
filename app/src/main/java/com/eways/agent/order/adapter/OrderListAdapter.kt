package com.eways.agent.order.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eways.agent.R
import com.eways.agent.order.activity.gantipaket.GantiPaketIsDoneActivity
import com.eways.agent.order.activity.gantipaket.GantiPaketOnProgressActivity
import com.eways.agent.order.activity.gantipaket.GantiPaketOnRequestActivity
import com.eways.agent.order.activity.layananbebas.LayananBebasIsDoneActivity
import com.eways.agent.order.activity.layananbebas.LayananBebasOnProgressActivity
import com.eways.agent.order.activity.layananbebas.LayananBebasOnRequestActivity
import com.eways.agent.order.activity.pickupticket.PickupTicketIsDoneActivity
import com.eways.agent.order.activity.pickupticket.PickupTicketOnProgressActivity
import com.eways.agent.order.activity.pickupticket.PickupTicketOnRequestActivity
import com.eways.agent.order.activity.psb.PSBIsDoneActivity
import com.eways.agent.order.activity.psb.PSBOnProgressActivity
import com.eways.agent.order.activity.psb.PSBOnRequestActivity
import com.eways.agent.order.activity.sopp.SOPPIsDoneActivity
import com.eways.agent.order.activity.sopp.SOPPOnProgressActivity
import com.eways.agent.order.activity.sopp.SOPPOnRequestActivity
import com.eways.agent.order.activity.titipbelanja.TitipBelanjaIsDoneActivity
import com.eways.agent.order.activity.titipbelanja.TitipBelanjaOnProgressActivity
import com.eways.agent.order.activity.titipbelanja.TitipBelanjaOnRequestActivity
import com.eways.agent.order.activity.titippaket.TitipPaketIsDoneActivity
import com.eways.agent.order.activity.titippaket.TitipPaketOnProgressActivity
import com.eways.agent.order.activity.titippaket.TitipPaketOnRequestActivity
import com.eways.agent.order.const.OrderStatus
import com.eways.agent.order.const.OrderType
import com.eways.agent.order.viewdto.IOrderViewDTO
import com.eways.agent.order.viewdto.OrderBasicViewDTO
import com.eways.agent.order.viewdto.OrderPacketViewDTO
import com.google.gson.Gson
import com.proyek.infrastructures.order.order.entities.Order
import com.proyek.infrastructures.order.order.entities.Orderable
import com.proyek.infrastructures.user.agent.entities.UserAgent
import kotlinx.android.synthetic.main.row_order_basic.view.*
import kotlinx.android.synthetic.main.row_order_basic.view.tvOrderTitle
import kotlinx.android.synthetic.main.row_order_packet.view.*

class OrderListAdapter(private val orders: List<IOrderViewDTO>, val user: UserAgent, val listOrder: ArrayList<Order>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        const val VIEW_TYPE_BASIC = 1
        const val VIEW_TYPE_PACKET = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_BASIC) {
            OrderBasicViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_order_basic, parent, false))
        } else OrderPacketViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_order_packet,parent,false))
    }

    override fun getItemCount(): Int =orders.size

    override fun getItemViewType(position: Int): Int {
        return if (orders[position] is OrderPacketViewDTO ) {
            VIEW_TYPE_PACKET
        } else VIEW_TYPE_BASIC
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (orders[position] is OrderPacketViewDTO) {
            (holder as OrderPacketViewHolder).bind(orders[position] as OrderPacketViewDTO, user)
        } else {
            (holder as OrderBasicViewHolder).bind(orders[position] as OrderBasicViewDTO, user, listOrder[position])
        }
    }
    inner class OrderBasicViewHolder internal constructor(view: View): RecyclerView.ViewHolder(view){
        @SuppressLint("SetTextI18n")
        fun bind(orderBasicViewDTO: OrderBasicViewDTO, user: UserAgent, order: Order){
            itemView.apply {
                val gson = Gson()

                val orderable = gson.fromJson(order.order, Orderable::class.java)

                if(order.customerId==order.agentId) {
                    tvOrderTitle.text = "Get customer - " + orderBasicViewDTO.orderType.value
                    tvCustomerName.text = "Nama: " + orderable.customer?.name
                    tvCustomerAddress.text = "Alamat: " + orderable.customer?.address
                } else {
                    tvOrderTitle.text = orderBasicViewDTO.orderType.value
                    tvCustomerName.text = "Nama: " + order.customer?.user?.user_name
                    tvCustomerAddress.text= "Alamat: " + order.customer?.user?.address
                }
                tvOrderDescription.text = orderBasicViewDTO.orderDescription

                tvOrderTime.text = orderBasicViewDTO.orderTime.getLocalizeDateString()

                setOnClickListener {
                    when(orderBasicViewDTO.orderType){
                        OrderType.PSB ->{
                            when(orderBasicViewDTO.orderStatus){
                                (OrderStatus.Created) ->{
                                    val intent = Intent(context, PSBOnRequestActivity::class.java)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }
                                (OrderStatus.OnProgress) -> {
                                    val intent = Intent(context, PSBOnProgressActivity::class.java)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }
                                (OrderStatus.CustomerFinished) -> {
                                    val intent = Intent(context, PSBOnProgressActivity::class.java)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }
                                (OrderStatus.AgentFinished) -> {
                                    val intent = Intent(context, PSBIsDoneActivity::class.java)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }
                                else -> {
                                    val intent = Intent(context, PSBIsDoneActivity::class.java)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }
                            }
                        }
                        OrderType.PickupTicket -> {
                            when(orderBasicViewDTO.orderStatus){
                                (OrderStatus.Created) ->{
                                    val intent = Intent(context, PickupTicketOnRequestActivity::class.java)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }

                                (OrderStatus.OnProgress) -> {
                                    val intent = Intent(context, PickupTicketOnProgressActivity::class.java)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }

                                (OrderStatus.CustomerFinished) -> {
                                    val intent = Intent(context, PickupTicketOnProgressActivity::class.java)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }
                                (OrderStatus.AgentFinished) -> {
                                    val intent = Intent(context, PickupTicketIsDoneActivity::class.java)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }
                                else -> {
                                    val intent = Intent(context, PickupTicketIsDoneActivity::class.java)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }
                            }
                        }

                        OrderType.GantiPaket -> {
                            when(orderBasicViewDTO.orderStatus){
                                (OrderStatus.Created) ->{
                                    val intent = Intent(context, GantiPaketOnRequestActivity::class.java)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }
                                (OrderStatus.OnProgress) -> {
                                    val intent = Intent(context, GantiPaketOnProgressActivity::class.java)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }

                                (OrderStatus.CustomerFinished) -> {
                                    val intent = Intent(context, GantiPaketOnProgressActivity::class.java)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }
                                (OrderStatus.AgentFinished) -> {
                                    val intent = Intent(context, GantiPaketIsDoneActivity::class.java)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }
                                else -> {
                                    val intent = Intent(context, GantiPaketIsDoneActivity::class.java)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }
                            }
                        }

                        OrderType.SOPP ->{
                            when(orderBasicViewDTO.orderStatus){
                                OrderStatus.Created -> {
                                    val intent = Intent(context, SOPPOnRequestActivity::class.java)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }
                                OrderStatus.AgentFinished ->{
                                    val intent = Intent(context, SOPPIsDoneActivity::class.java)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }
                                else->{
                                    val intent = Intent(context, SOPPOnProgressActivity::class.java)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }
                            }
                        }

                        OrderType.LayananBebas->{
                            when(orderBasicViewDTO.orderStatus){
                                OrderStatus.Created -> {
                                    val intent = Intent(context, LayananBebasOnRequestActivity::class.java)
                                    Log.d("masukid", orderBasicViewDTO.id)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }
                                OrderStatus.AgentFinished ->{
                                    val intent = Intent(context, LayananBebasIsDoneActivity::class.java)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }
                                else->{
                                    val intent = Intent(context, LayananBebasOnProgressActivity::class.java)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }
                            }
                        }
                        OrderType.TitipPaket->{
                            when(orderBasicViewDTO.orderStatus){
                                OrderStatus.Created -> {
                                    val intent = Intent(context, TitipPaketOnRequestActivity::class.java)
                                    Log.d("masukid", orderBasicViewDTO.id)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }
                                OrderStatus.AgentFinished ->{
                                    val intent = Intent(context, TitipPaketIsDoneActivity::class.java)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }
                                else->{
                                    val intent = Intent(context, TitipPaketOnProgressActivity::class.java)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }
                            }
                        }
                        OrderType.TitipBelanja->{
                            when(orderBasicViewDTO.orderStatus){
                                OrderStatus.Created -> {
                                    val intent = Intent(context, TitipBelanjaOnRequestActivity::class.java)
                                    Log.d("masukid", orderBasicViewDTO.id)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }
                                OrderStatus.AgentFinished ->{
                                    val intent = Intent(context, TitipBelanjaIsDoneActivity::class.java)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }
                                else->{
                                    val intent = Intent(context, TitipBelanjaOnProgressActivity::class.java)
                                    intent.putExtra("id", orderBasicViewDTO.id)
                                    intent.putExtra("user", user)
                                    context.startActivity(intent)
                                }
                            }
                        }

                    }
                }
            }
        }
    }

    inner class OrderPacketViewHolder internal constructor(view:View): RecyclerView.ViewHolder(view){
        fun bind(orderPacketViewDTO: OrderPacketViewDTO, user: UserAgent){
            itemView.apply {
                tvOrderTitle.text = orderPacketViewDTO.orderType.value
                tvOrderSenderName.text = orderPacketViewDTO.orderSenderName
                tvOrderSenderAddress.text = orderPacketViewDTO.orderSenderAddress
                tvOrderReceiverName.text = orderPacketViewDTO.orderReceiverName
                tvOrderReceiverAddress.text = orderPacketViewDTO.orderReceiverAddress

                setOnClickListener {
                    when(orderPacketViewDTO.orderStatus){
                        OrderStatus.Created -> {
                            val intent = Intent(context, TitipPaketOnRequestActivity::class.java)
                            intent.putExtra("id", orderPacketViewDTO.id)
                            intent.putExtra("user", user)
                            context.startActivity(intent)
                        }
                        OrderStatus.AgentFinished ->{
                            val intent = Intent(context, TitipPaketIsDoneActivity::class.java)
                            intent.putExtra("id", orderPacketViewDTO.id)
                            intent.putExtra("user", user)
                            context.startActivity(intent)
                        }
                        else->{
                            val intent = Intent(context, TitipPaketOnProgressActivity::class.java)
                            intent.putExtra("id", orderPacketViewDTO.id)
                            intent.putExtra("user", user)
                            context.startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}