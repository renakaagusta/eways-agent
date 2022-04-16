package com.proyek.infrastructures.order.chat.network

import com.proyek.infrastructures.order.chat.entities.Chat
import java.lang.Error

data class ChatResponse(
    val errors: Error,
    val message: String,
    val data: List<Chat>
)