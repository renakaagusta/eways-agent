package com.eways.agent.order.viewdto

import com.eways.agent.utils.date.SLDate

data class ChatViewDTO(
    val isBelongToCurrentUser : Boolean,
    val text: String,
    val time: SLDate
)