package com.eways.agent.notification.activity

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.notification.adapter.NotificationAdapter
import com.eways.agent.notification.const.NotificationType
import com.eways.agent.notification.viewdto.NotificationViewDTO
import com.eways.agent.order.const.OrderStatus
import com.eways.agent.order.const.OrderType
import com.eways.agent.utils.customitemdecoration.CustomVerticalItemDecoration
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.eways.agent.utils.date.SLDate
import com.proyek.infrastructures.notification.usecases.GetNotificationByUserId
import com.proyek.infrastructures.order.order.usecases.GetOrderDetail
import com.proyek.infrastructures.user.agent.entities.UserAgent
import kotlinx.android.synthetic.main.activity_notification.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.google.gson.Gson
import com.proyek.infrastructures.notification.usecases.ReadNotification
import com.proyek.infrastructures.order.order.entities.Order
import com.proyek.infrastructures.order.order.entities.Orderable
import com.proyek.infrastructures.utils.Authenticated
import java.text.SimpleDateFormat

class NotificationActivity : BaseActivity(){
    private lateinit var user: UserAgent

    private lateinit var getNotificationByUserId: GetNotificationByUserId
    private lateinit var getOrderDetail: GetOrderDetail
    private lateinit var readNotification: ReadNotification

    private val listNotificationId = ArrayList<String>()
    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        CustomSupportActionBar.setCustomActionBar(this, "Notifikasi")
    }

    override fun onStart() {
        super.onStart()

        this@NotificationActivity.showProgress()

        user = Authenticated.getUserAgent()
        getNotificationByUserId = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetNotificationByUserId::class.java)
        getOrderDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetOrderDetail::class.java)
        readNotification = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ReadNotification::class.java)

        GlobalScope.launch(context = Dispatchers.Main) {
            val gson  = Gson()
            var dtoNotification = ArrayList<NotificationViewDTO>()

            getNotificationByUserId.set( this@NotificationActivity,user.ID!!)
            delay(300)
            getNotificationByUserId.get().apply {

                Log.d("notificationData", this.toString())

                if (this.data?.isNotEmpty()!!) {
                    for (i in 0 until this.data?.size!!) {
                        if(this.data!![i].type==2)
                            break
                        val it = this.data!![i]

                        listNotificationId.add(this.data!![i].id!!)

                        val SLDate = SLDate()
                        SLDate.date = SimpleDateFormat("yyyy-MM-dd").parse(it.createdAt)

                        getOrderDetail.set(this@NotificationActivity, it.typeId!!)
                        delay(300)

                        for(j in 0 until getOrderDetail.get().size) {
                            val thisOrder = getOrderDetail.get()[j]

                            if (thisOrder != Order()) {
                                val order: Orderable =
                                    gson.fromJson(thisOrder.order, Orderable::class.java)

                                val orderType = orderType(order.service?.type!!)
                                val orderStatus = orderStatus(thisOrder.orderStatus!!)
                                val notificationType = notificationType(thisOrder.orderStatus!!)

                                dtoNotification.add(
                                    NotificationViewDTO(
                                        notificationType,
                                        SLDate,
                                        orderType?.value!!,
                                        2000,
                                        orderStatus.toString()
                                    )
                                )
                            }
                            delay(300)
                        }

                        getOrderDetail.get().clear()
                    }

                    setNotificationData(dtoNotification)

                    readNotification.set(listNotificationId)
                }

                this@NotificationActivity.dismissProgress()
            }
        }

    }

    override fun onRestart() {
        super.onRestart()

        getOrderDetail.get().clear()
        notificationAdapter.clear()
    }

    private fun notificationType(orderStatus: Int) : NotificationType {
        return when(orderStatus){
            0->NotificationType.Created
            1->NotificationType.OnProgress
            2->NotificationType.OnProgress
            3->NotificationType.Finished
            else->NotificationType.Created
        }
    }


    private fun orderStatus(status: Int): OrderStatus? {
        return when(status){
            0-> OrderStatus.Created
            1-> OrderStatus.OnProgress
            2-> OrderStatus.CustomerFinished
            else-> null
        }
    }

    private fun orderType(type: Int): OrderType? {
        return when(type){
            1-> OrderType.PSB
            2-> OrderType.PickupTicket
            3-> OrderType.GantiPaket
            4-> OrderType.TitipPaket
            5-> OrderType.SOPP
            6-> OrderType.TitipBelanja
            7-> OrderType.LayananBebas
            else-> null
        }
    }


    private fun setNotificationData(dtoNotification: ArrayList<NotificationViewDTO>){
        //val itemDecoration = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        notificationAdapter = NotificationAdapter(dtoNotification)
        rvNotification.apply {
            layoutManager = LinearLayoutManager(this@NotificationActivity)
            //addItemDecoration(CustomDividerItemDecoration(ContextCompat.getDrawable(this@KabarClusterActivity, R.drawable.divider_kabarcluster)!!))
            addItemDecoration(CustomVerticalItemDecoration(15))
            adapter = notificationAdapter
        }
    }
}