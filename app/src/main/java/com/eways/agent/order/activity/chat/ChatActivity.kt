package com.eways.agent.order.activity.chat

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.order.adapter.ChatAdapter
import com.eways.agent.order.viewdto.ChatViewDTO
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.eways.agent.utils.date.SLDate
import com.eways.agent.utils.firebase.firestore.Firestore
import com.google.firebase.firestore.ListenerRegistration
import com.proyek.infrastructures.notification.usecases.CreateChatNotification
import com.proyek.infrastructures.order.chat.entities.Chat
import com.proyek.infrastructures.order.chat.usecases.CreateChat
import com.proyek.infrastructures.user.user.usecases.GetUserDetail
import kotlinx.android.synthetic.main.activity_chat.*
import java.text.SimpleDateFormat

class ChatActivity : BaseActivity() {
    private lateinit var getUserDetail: GetUserDetail
    private lateinit var chatListener: ListenerRegistration
    private lateinit var createChat: CreateChat
    private lateinit var createNotification: CreateChatNotification

    private lateinit var agentId: String
    private lateinit var agentUserName: String
    private lateinit var customerId: String
    private lateinit var customerUserName: String
    private lateinit var orderId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
    }

    override fun onStart() {
        super.onStart()

        createChat = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(CreateChat::class.java)
        createNotification = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(CreateChatNotification::class.java)
        getUserDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetUserDetail::class.java)

        agentId = intent.getStringExtra("agentId")
        agentUserName = intent.getStringExtra("agentUserName")
        customerId = intent.getStringExtra("customerId")
        customerUserName = intent.getStringExtra("customerUserName")
        orderId = intent.getStringExtra("orderId")

        CustomSupportActionBar.setCustomActionBar(this, customerUserName)
        chatListener = Firestore.ChatListener(orderId, this::setChatData)

        sendChat()
    }

    private fun sendChat() {
        ivSend.setOnClickListener {
            createChat.set(agentId, customerId, etChat.text.toString(), orderId, agentUserName, etChat.text.toString(), this@ChatActivity)
            createChat.get().observe(this, Observer {
                Log.d("chat", it.toString())
            })
            etChat.setText("")
        }
    }


    private fun setChatData(chats: ArrayList<Chat>) {
        val listChatViewDTO = ArrayList<ChatViewDTO>()

        chats.forEach {
            val SLDate = SLDate()
            SLDate.date = SimpleDateFormat("yyyy-MM-dd").parse(it.created_at)

            listChatViewDTO.add(
                ChatViewDTO(
                    it.sender_id == agentId,
                    it.content!!,
                    SLDate
                )
            )
        }

        val charAdapter = ChatAdapter(listChatViewDTO)
        rvChat.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = charAdapter
        }
    }
}