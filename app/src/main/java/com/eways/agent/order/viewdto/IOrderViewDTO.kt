package com.eways.agent.order.viewdto

import com.eways.agent.order.const.OrderStatus
import com.eways.agent.order.const.OrderType

interface IOrderViewDTO {
    val orderType : OrderType
    val orderStatus : OrderStatus
}