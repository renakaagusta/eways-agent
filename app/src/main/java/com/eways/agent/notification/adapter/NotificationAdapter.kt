package com.eways.agent.notification.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.eways.agent.R
import com.eways.agent.notification.const.NotificationType
import com.eways.agent.notification.viewdto.NotificationViewDTO
import kotlinx.android.synthetic.main.row_notification.view.*

class NotificationAdapter(private val notifications:ArrayList<NotificationViewDTO>) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>(){

    fun clear() {
        val size: Int = notifications.size
        notifications.clear()
        notifyItemRangeRemoved(0, size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_notification, parent, false))
    }

    override fun getItemCount(): Int =notifications.size

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bindKabarCluster(notifications[position])
    }
    inner class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindKabarCluster(notificationViewDTO: NotificationViewDTO) {
            itemView.apply {
                tvNotificationType.text =notificationViewDTO.notificationType.value
                tvDate.text = notificationViewDTO.date.getLocalizeDateString()
                tvOrderType.text = notificationViewDTO.orderType
                if(notificationViewDTO.notificationType == NotificationType.Billing){
                    tvBilling.isVisible = true
                    tvBilling.text = notificationViewDTO.price.toString()
                }
            }
        }
    }
}