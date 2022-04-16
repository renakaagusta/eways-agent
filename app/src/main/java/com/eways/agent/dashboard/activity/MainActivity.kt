package com.eways.agent.dashboard.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.order.activity.HistoryFragment
import com.eways.agent.order.activity.OrderFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.iid.FirebaseInstanceId
import com.proyek.infrastructures.notification.usecases.GetNotificationByUserId
import com.proyek.infrastructures.notification.usecases.GetUnreadNotificationByUserId
import com.proyek.infrastructures.notification.usecases.ReadNotification
import com.proyek.infrastructures.order.order.usecases.GetOrderDetail
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.user.agent.usecases.GetAgentDetail
import com.proyek.infrastructures.utils.Authenticated
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainActivity : BaseActivity() {
    var selectedFragment: Fragment? = null
    private lateinit var user: UserAgent

    private lateinit var getNotificationByUserId: GetUnreadNotificationByUserId
    private lateinit var getOrderDetail: GetOrderDetail
    private lateinit var readNotification: ReadNotification
    private lateinit var getUserAgentDetail: GetAgentDetail

    private lateinit var savedInstanceState: Bundle

    private var listOrderNotification = ArrayList<String>()
    private var listHomeNotification = ArrayList<String>()
    private var listHistoryNotification = ArrayList<String>()

    private lateinit var badgeDrawableOrder: BadgeDrawable
    private lateinit var badgeDrawableHome: BadgeDrawable
    private lateinit var badgeDrawableHistory: BadgeDrawable

    private var bottomIndexItem = R.id.navHome

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        user = Authenticated.getUserAgent()

        getNotificationByUserId = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetUnreadNotificationByUserId::class.java)
        getOrderDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetOrderDetail::class.java)
        readNotification = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ReadNotification::class.java)

        badgeDrawableOrder = bottomNavigation.getOrCreateBadge(R.id.navOrder)
        badgeDrawableHome = bottomNavigation.getOrCreateBadge(R.id.navHome)
        badgeDrawableHistory = bottomNavigation.getOrCreateBadge(R.id.navHistory)

        badgeDrawableOrder.setVisible(false)
        badgeDrawableHome.setVisible(false)
        badgeDrawableHistory.setVisible(false)

        if (Authenticated.isValidCacheMember()){
            createFragment(savedInstanceState)
        } else {
            showProgress()
            getUserAgentDetail.set(Authenticated.getUserAgent().ID!!, this)
            getUserAgentDetail.get().observe(this, Observer {
                Authenticated.setUserAgent(it.data[0])
                createFragment(savedInstanceState)

                dismissProgress()
            })
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBackPressed() {
        if(bottomIndexItem == R.id.navHome)
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah anda yakin ingin keluar dari aplikasi?")
                .setConfirmButton("Ya",{
                    System.exit(0)
                    finishAndRemoveTask()
                })
                .setCancelText("Tidak")
                .show()
        else
            bottomNavigation.selectedItemId = R.id.navHome
    }

    private fun createFragment(savedInstanceState: Bundle?) {
        user = Authenticated.getUserAgent()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragmentContainer,
                HomeFragment(user)
            ).commit()
        }
        bottomNavigation.setOnNavigationItemSelectedListener(navListener)

        bottomNavigation.selectedItemId = bottomIndexItem
        bottomNavigation.menu.findItem(bottomIndexItem).isChecked
    }

    override fun onResume() {
        super.onResume()

        user = Authenticated.getUserAgent()

        getNotificationByUserId =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                GetUnreadNotificationByUserId::class.java
            )
        getOrderDetail = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(GetOrderDetail::class.java)
        readNotification = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(ReadNotification::class.java)

        bottomNavigation.selectedItemId = bottomIndexItem
        bottomNavigation.menu.findItem(bottomIndexItem).isChecked

        listHomeNotification = ArrayList()
        listOrderNotification = ArrayList()
        listHistoryNotification = ArrayList()

        setNotification(user)
    }

    override fun onRestart() {
        super.onRestart()
    }



        private fun setNotification(user: UserAgent?) {
            GlobalScope.launch(Dispatchers.Main) {
                var listOrderId = ArrayList<String>()

                getNotificationByUserId.set(this@MainActivity, user?.ID!!)
                delay(1000)
                getNotificationByUserId.get().data?.forEach {
                    if (it.readAt == null && it.type == 1) {
                        listOrderId.add(it.typeId!!)

                    }
                }


                delay(500)

                listOrderId.forEach {
                    getOrderDetail.set(this@MainActivity, it)
                    delay(500)
                    for (it in getOrderDetail.get()) {
                        when (it.orderStatus!!) {
                            0 -> listHomeNotification.add(it.id!!)
                            in 1..2 -> listOrderNotification.add(it.id!!)
                            3 -> listHistoryNotification.add(it.id!!)
                        }
                        break
                    }
                }

                delay(500)

                if (listOrderNotification.isNotEmpty()) {
                    if (!listOrderNotification.isNullOrEmpty())badgeDrawableOrder.setVisible(true)
                    badgeDrawableOrder.number = listOrderNotification.size
                    badgeDrawableOrder.backgroundColor = R.drawable.bottomnav_item
                }

                if (listHomeNotification.isNotEmpty()) {
                    if (!listHomeNotification.isNullOrEmpty())badgeDrawableHome.setVisible(true)
                    badgeDrawableHome.number = listHomeNotification.size
                    badgeDrawableHome.backgroundColor = R.drawable.bottomnav_item
                }

                if (listHistoryNotification.isNotEmpty()) {
                    if (!listHistoryNotification.isNullOrEmpty())badgeDrawableHistory.setVisible(true)
                    badgeDrawableHistory.number = listHistoryNotification.size
                    badgeDrawableHistory.backgroundColor = R.drawable.bottomnav_item
                }
            }
        }

        private val navListener: BottomNavigationView.OnNavigationItemSelectedListener =
            object : BottomNavigationView.OnNavigationItemSelectedListener {
                override fun onNavigationItemSelected(item: MenuItem): Boolean {
                    selectedFragment = null
                    when (item.itemId) {
                        R.id.navHome -> {
                            if(listHomeNotification.size>0)
                            readNotification.set(listHomeNotification)
                            selectedFragment = HomeFragment(user)
                            bottomIndexItem = item.itemId

                        }
                        R.id.navHistory -> {
                            if(listHistoryNotification.size>0)
                            readNotification.set(listHistoryNotification)
                            selectedFragment = HistoryFragment(user)
                            bottomIndexItem = item.itemId

                        }
                        R.id.navOrder -> {
                            if(listOrderNotification.size>0)
                            readNotification.set(listOrderNotification)
                            selectedFragment = OrderFragment(user)
                            bottomIndexItem = item.itemId

                        }
                    }

                    bottomNavigation.getBadge(item.itemId)?.let { badgeDrawable ->
                        if (badgeDrawable.isVisible) bottomNavigation.removeBadge(item.itemId)
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, selectedFragment!!).commit()
                    return true
                }
            }
}
