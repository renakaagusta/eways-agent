package com.eways.agent.order.activity.getcustomer.option

import android.os.Bundle
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.order.adapter.OrderTypeOptionAdapter
import com.eways.agent.order.const.OrderType
import com.eways.agent.utils.customitemdecoration.CustomDividerItemDecoration
import kotlinx.android.synthetic.main.activity_option.*

class OrderTypeOptionActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option)
        supportActionBar?.hide()

        setSOPPData()
    }

    private fun setSOPPData(){
        val listOrderType = mutableListOf(
            OrderType.PSB.value, OrderType.PickupTicket.value, OrderType.GantiPaket.value,
            OrderType.SOPP.value, OrderType.TitipPaket.value, OrderType.LayananBebas.value , OrderType.TitipBelanja.value
        )
        val optionAdapter =
            OrderTypeOptionAdapter(
                listOrderType
            )
        rvOption.apply {
            layoutManager = LinearLayoutManager(this@OrderTypeOptionActivity)
            addItemDecoration(CustomDividerItemDecoration(ContextCompat.getDrawable(this@OrderTypeOptionActivity, R.drawable.divider_line)!!))
            isNestedScrollingEnabled = false
            adapter = optionAdapter
        }

        svOption.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                optionAdapter.filter.filter(newText)
                return false
            }

        })
    }
}