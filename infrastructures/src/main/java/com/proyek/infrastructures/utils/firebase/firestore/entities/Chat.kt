package com.proyek.infrastructures.utils.firebase.firestore.entities

import java.util.*

data class Chat(val text: String = "",
                val time: Date? = null,
                val senderId: String = "",
                val type: String = "TEXT")