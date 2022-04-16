package com.eways.agent.order.viewdto

import com.eways.agent.order.const.OrderStatus
import com.eways.agent.order.const.OrderType
import com.eways.agent.utils.date.SLDate

class OrderPacketViewDTO (
    val id: String,
    override val orderType: OrderType,
    val orderSenderName: String,
    val orderSenderAddress: String,
    val orderReceiverName: String,
    val orderReceiverAddress: String,
    val orderTime: SLDate,
    override val orderStatus: OrderStatus
):IOrderViewDTO