package com.eways.agent.order.activity.getcustomer.option

import android.os.Bundle
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.order.adapter.SOPPOptionAdapter
import com.eways.agent.utils.customitemdecoration.CustomDividerItemDecoration
import com.proyek.infrastructures.inventory.invoice.entities.Invoice
import com.proyek.infrastructures.inventory.invoice.usecases.GetInvoiceList
import kotlinx.android.synthetic.main.activity_option.*

class SOPPOptionActivity :BaseActivity() {
    private lateinit var getInvoiceList: GetInvoiceList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_option)
        supportActionBar?.hide()

        getInvoiceList = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            GetInvoiceList::class.java)

        setSOPPData()
    }

    private fun setSOPPData(){

        this@SOPPOptionActivity.showProgress()

        getInvoiceList.set(this@SOPPOptionActivity)
        getInvoiceList.get().observe(this, Observer {
            this@SOPPOptionActivity.dismissProgress()

            val listSOPP = ArrayList<Invoice>()

            it.data.forEach {
                listSOPP.add(it)
            }

            val optionAdapter = SOPPOptionAdapter(listSOPP)
            rvOption.apply {
                layoutManager = LinearLayoutManager(this@SOPPOptionActivity)
                addItemDecoration(CustomDividerItemDecoration(ContextCompat.getDrawable(this@SOPPOptionActivity, R.drawable.divider_line)!!))
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
        })
    }
}