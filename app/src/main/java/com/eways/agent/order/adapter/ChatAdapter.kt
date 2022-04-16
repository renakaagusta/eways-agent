package com.eways.agent.order.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eways.agent.R
import com.eways.agent.order.viewdto.ChatViewDTO
import kotlinx.android.synthetic.main.row_chat_mine.view.*
import kotlinx.android.synthetic.main.row_chat_other.view.*

class ChatAdapter(private val chats: List<ChatViewDTO>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object{
        const val VIEW_TYPE_FROM_ME = 1
        const val VIEW_TYPE_FROM_OTHER = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_FROM_ME) {
            ChatFromMeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_chat_mine, parent, false))
        } else ChatFromOtherViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_chat_other,parent,false))
    }

    override fun getItemCount(): Int =chats.size

    override fun getItemViewType(position: Int): Int {
        return if (chats[position].isBelongToCurrentUser) {
            VIEW_TYPE_FROM_ME
        } else VIEW_TYPE_FROM_OTHER
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (chats[position].isBelongToCurrentUser) {
            (holder as ChatFromMeViewHolder).bind(chats[position])
        } else {
            (holder as ChatFromOtherViewHolder).bind(chats[position])
        }
    }
    inner class ChatFromMeViewHolder internal constructor(view: View): RecyclerView.ViewHolder(view){
        fun bind(chatViewDTO: ChatViewDTO){
            itemView.apply {
                tvMyMessage.text = chatViewDTO.text
                tvMyMessageTime.text = chatViewDTO.time.getLocalizeDateString()
            }
        }
    }

    inner class ChatFromOtherViewHolder internal constructor(view:View): RecyclerView.ViewHolder(view){
        fun bind(chatViewDTO: ChatViewDTO){
            itemView.apply {
                tvOthersMessage.text = chatViewDTO.text
                tvOthersMessageTime.text = chatViewDTO.time.getLocalizeDateString()
            }
        }
    }
}