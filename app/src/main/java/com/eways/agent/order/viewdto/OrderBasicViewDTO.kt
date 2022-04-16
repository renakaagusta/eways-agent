package com.eways.agent.order.viewdto

import com.eways.agent.order.const.OrderStatus
import com.eways.agent.order.const.OrderType
import com.eways.agent.utils.date.SLDate

data class OrderBasicViewDTO(
    val id: String,
    override val orderType: OrderType,
    val orderDescription: String,
    val orderTime: SLDate,
    override val orderStatus: OrderStatus
):IOrderViewDTO