package com.proyek.infrastructures.order.chat.entities

import com.google.gson.annotations.SerializedName

data class ChatRoom(
    @SerializedName("chats")
    val chats: List<Chat>
)