package com.eways.agent.dashboard.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eways.agent.R
import com.eways.agent.kabarcluster.activity.KabarClusterActivity
import com.eways.agent.kabarcluster.adapter.KabarClusterPostAdapter
import com.eways.agent.kabarcluster.viewdto.KabarClusterPostViewDTO
import com.eways.agent.notification.activity.NotificationActivity
import com.eways.agent.order.activity.gantipaket.GantiPaketListActivity
import com.eways.agent.order.activity.pickupticket.PickupTicketListActivity
import com.eways.agent.order.activity.psb.PSBListActivity
import com.eways.agent.order.const.OrderType
import com.eways.agent.order.activity.getcustomer.GetCustomerFormActivity
import com.eways.agent.order.activity.layananbebas.LayananBebasListActivity
import com.eways.agent.order.activity.sopp.SOPPListActivity
import com.eways.agent.order.activity.titipbelanja.TitipBelanjaListActivity
import com.eways.agent.order.activity.titippaket.TitipPaketListActivity
import com.eways.agent.user.activity.ProfileActivity
import com.eways.agent.utils.customitemdecoration.CustomDividerItemDecoration
import com.eways.agent.utils.firebase.firestore.Firestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.gson.Gson
import com.proyek.infrastructures.kabarcluster.comment.usecases.GetCommentList
import com.proyek.infrastructures.kabarcluster.post.usecases.GetPostList
import com.proyek.infrastructures.notification.usecases.GetNotificationByUserId
import com.proyek.infrastructures.notification.usecases.ReadNotification
import com.proyek.infrastructures.order.order.entities.Orderable
import com.proyek.infrastructures.order.order.usecases.GetOrderDetail
import com.proyek.infrastructures.order.order.usecases.GetAgentCreatedOrder
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.user.user.usecases.GetUserDetail
import com.proyek.infrastructures.utils.Authenticated
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment(user: UserAgent) : Fragment() {
    private var user = user
    private lateinit var rvPost: RecyclerView
    private lateinit var tvName: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var getPostList: GetPostList
    private lateinit var getUserDetail: GetUserDetail
    private lateinit var getCommentList: GetCommentList
    private lateinit var getOrderDetail: GetOrderDetail
    private lateinit var getNotification: GetNotificationByUserId
    private lateinit var getAgentCreatedOrder: GetAgentCreatedOrder
    private lateinit var readNotificaton: ReadNotification

    private lateinit var postListener: ListenerRegistration

    private lateinit var myView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        tvName = view.findViewById<TextView>(R.id.tvName)
        progressBar = view.findViewById(R.id.progressBar)

        rvPost = view.findViewById<RecyclerView>(R.id.rvKabarCluster)
        rvPost.setHasFixedSize(true)
        rvPost.layoutManager = LinearLayoutManager(activity)

        getUserDetail = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(GetUserDetail::class.java)
        getPostList = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(GetPostList::class.java)
        getCommentList = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(GetCommentList::class.java)
        getOrderDetail = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(GetOrderDetail::class.java)
        getNotification = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            GetNotificationByUserId::class.java
        )
        getAgentCreatedOrder = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(GetAgentCreatedOrder::class.java)
        readNotificaton = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(ReadNotification::class.java)

        if(user.cluster!=null)
            postListener = Firestore.PostListener(user.cluster?.ID!!, view, this::showKabarCluster)
        else
            postListener = Firestore.PostListener("1", view, this::showKabarCluster)

        myView = view

        setNotification(view)

        moveToPSBList(view)
        moveToGantiPaketList(view)
        moveToPickupTicketList(view)

        moveToProfile(view)
        moveToNotification(view)
        moveTOPSB(view)
        moveToGantiPaket(view)
        moveToPickupTicket(view)
        moveToTitipPaket(view)
        moveToTitipBelanja(view)
        moveToSOPP(view)
        moveToLayananBebas(view)
        moveToGetCustomer(view)
        moveToKabarCluster(view)
        return view
    }

    override fun onStart() {
        super.onStart()

        user = Authenticated.getUserAgent()

        tvName.text = user.fullname.toString()

        if (user.imagePath != null)
            Glide.with(this@HomeFragment.myView)
                .load("http://13.229.200.77:8001/storage/${user.imagePath}")
                .into(this@HomeFragment.myView.imgProfile)
    }

    private fun showKabarCluster(view: View?, dtoPost: ArrayList<KabarClusterPostViewDTO>) {
        progressBar.visibility = View.VISIBLE
        rvPost.visibility = View.GONE

        GlobalScope.launch(Dispatchers.Main) {
            var index = 0
            dtoPost.forEach {
                val dto = it
                getUserDetail.set(it.userId, view?.context!!)
                delay(700)
                getUserDetail.get().apply {
                    if(!getUserDetail.get().isNullOrEmpty()) {
                        dto.creator = this[0].data[0].username!!
                        if (this[0].data[0].imagePath != null) dto.imagePath =
                            this[0].data[0].imagePath!!
                    }
                }
                dtoPost[index] = dto
                index++
            }

            index = 0

            delay(700)

            dtoPost.forEach {
                val dto = it
                getCommentList.set(it.id, view?.context!!)
                delay(500)
                getCommentList.get().apply {
                    var commentCount = 0
                    if(!this.data.isNullOrEmpty())this.data!!.forEach {
                        commentCount++
                    }
                    dto.commentCount = commentCount
                }
                dtoPost[index] = dto
                index++
            }

            delay(700)

            progressBar.visibility = View.GONE
            rvPost.visibility = View.VISIBLE

            setKabarClusterData(view!!, dtoPost)
        }
    }

    private fun setNotification(view: View) {
        GlobalScope.launch(Dispatchers.Main) {
            val gson = Gson()

            var psb = 0
            var gantiPaket = 0
            var pickupTicket = 0
            var titipPaket = 0
            var sopp = 0
            var titipBelanja = 0
            var layananBebas = 0

            val listNotification = ArrayList<String>()
            val listUnreadNotification = ArrayList<String>()

            view.ivNotificationAlert.visibility = View.GONE

            getNotification.set(view.context, user.ID!!)
            delay(1000)

            getNotification.get().data?.forEach {
                    if ((it.readAt == null) && (it.type == 1)) {
                        listNotification.add(it.typeId!!)
                    }
                    if(it.readAt == null) {
                        listUnreadNotification.add(it.typeId!!)
                    }

            }

            delay(300)

            listNotification.forEach {
                getOrderDetail.set(view.context,it)
                delay(500)
                for(order in getOrderDetail.get()) {
                    if(order.orderStatus == 0) {
                        val orderable = gson.fromJson(order.order, Orderable::class.java)
                        when (orderable.service?.type) {
                            1 -> psb++
                            2 -> pickupTicket++
                            3 -> gantiPaket++
                            4 -> titipPaket++
                            5 -> sopp++
                            6 -> titipBelanja++
                            7 -> layananBebas++
                        }
                        getOrderDetail.get().clear()
                        break
                    }
                }
            }


                delay(1000)

                if (psb != 0) {
                    view.tvPSBNotification.visibility = View.VISIBLE
                    view.tvPSBNotification.text = psb.toString()
                }

                if (pickupTicket != 0) {
                    view.tvPickupTicketNotification.visibility = View.VISIBLE
                    view.tvPickupTicketNotification.text = pickupTicket.toString()
                }

                if (gantiPaket != 0) {
                    view.tvGantiPaketNotification.visibility = View.VISIBLE
                    view.tvGantiPaketNotification.text = gantiPaket.toString()
                }

                if (titipPaket != 0) {
                    view.tvTitipPaketNotification.visibility = View.VISIBLE
                    view.tvTitipPaketNotification.text = titipPaket.toString()
                }

                if (sopp != 0) {
                    view.tvSOPPNotification.visibility = View.VISIBLE
                    view.tvSOPPNotification.text = sopp.toString()
                }

                if (titipBelanja != 0) {
                    view.tvTitipBelanjaNotification.visibility = View.VISIBLE
                    view.tvTitipBelanjaNotification.text = titipBelanja.toString()
                }

                if (layananBebas != 0) {
                    view.tvLayananBebasNotification.visibility = View.VISIBLE
                    view.tvLayananBebasNotification.text = layananBebas.toString()
                }

            if(listUnreadNotification.isNullOrEmpty())
                view.ivNotificationAlert.visibility = View.GONE
            else
                view.ivNotificationAlert.visibility = View.VISIBLE

            readNotificaton.set(listUnreadNotification)
        }
    }


    private fun moveToProfile(view: View) {
        view.llUser.setOnClickListener {
            val intent = Intent(view.context, ProfileActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }

    private fun moveTOPSB(view: View) {
        view.llPSB.setOnClickListener {
            val intent = Intent(activity?.applicationContext, PSBListActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }

    private fun moveToPickupTicket(view: View) {
        view.llPickupTicket.setOnClickListener {
            val intent = Intent(activity?.applicationContext, PickupTicketListActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }

    private fun moveToGantiPaket(view: View) {
        view.llGantiPaket.setOnClickListener {
            val intent = Intent(activity?.applicationContext, GantiPaketListActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }

    private fun moveToSOPP(view: View) {
        view.llSOPP.setOnClickListener {
            val intent = Intent(activity?.applicationContext, SOPPListActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }

    private fun moveToTitipPaket(view: View) {
        view.llTitipPaket.setOnClickListener {
            val intent = Intent(activity?.applicationContext, TitipPaketListActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }

    private fun moveToTitipBelanja(view: View) {
        view.llTitipBelanja.setOnClickListener {
            val intent = Intent(activity?.applicationContext, TitipBelanjaListActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }

    private fun moveToLayananBebas(view: View) {
        view.llLayananBebas.setOnClickListener {
            val intent = Intent(activity?.applicationContext, LayananBebasListActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }

    private fun moveToGetCustomer(view: View) {
        view.llGetCustomer.setOnClickListener {
            val intent = Intent(activity?.applicationContext, GetCustomerFormActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }

    private fun moveToNotification(view: View) {
        view.ivNotification.setOnClickListener {
            val intent = Intent(view.context, NotificationActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }


    private fun moveToPSBList(view: View) {
        view.llPSB.setOnClickListener {
            val intent = Intent(view.context, PSBListActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }

    private fun moveToGantiPaketList(view: View) {
        view.llGantiPaket.setOnClickListener {
            val intent = Intent(view.context, GantiPaketListActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }

    private fun moveToPickupTicketList(view: View) {
        view.llPickupTicket.setOnClickListener {
            val intent = Intent(view.context, PickupTicketListActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }


    private fun moveToKabarCluster(view: View) {
        view.tvKabarClusterLainnya.setOnClickListener {
            val intent = Intent(view.context, KabarClusterActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }

    private fun setKabarClusterData(view: View, dtoPost: ArrayList<KabarClusterPostViewDTO>) {

        lateinit var kabarClusterAdapter: KabarClusterPostAdapter

        if(user.cluster!=null)
            kabarClusterAdapter = KabarClusterPostAdapter(dtoPost, user.cluster?.ID!!, user.ID!!)
        else
            kabarClusterAdapter = KabarClusterPostAdapter(dtoPost, "1", user.ID!!)

        val decorator = DividerItemDecoration(view.context, LinearLayoutManager.VERTICAL)
        decorator.setDrawable(
            ContextCompat.getDrawable(
                view.context,
                R.drawable.divider_kabarcluster
            )!!
        )
        view.rvKabarCluster.apply {
            layoutManager = LinearLayoutManager(view.context)
            addItemDecoration(
                CustomDividerItemDecoration(
                    ContextCompat.getDrawable(
                        view.context,
                        R.drawable.divider_kabarcluster
                    )!!
                )
            )
            isNestedScrollingEnabled = false
            adapter = kabarClusterAdapter
        }
    }

    private fun orderType(type: Int): OrderType? {
        return when (type) {
            1 -> OrderType.PSB
            2 -> OrderType.PickupTicket
            3 -> OrderType.GantiPaket
            4 -> OrderType.TitipPaket
            5 -> OrderType.SOPP
            6 -> OrderType.TitipBelanja
            7 -> OrderType.LayananBebas
            else -> null
        }
    }
}