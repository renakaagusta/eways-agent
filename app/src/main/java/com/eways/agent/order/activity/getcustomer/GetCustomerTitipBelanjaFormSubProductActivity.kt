package com.eways.agent.order.activity.getcustomer

import android.content.Intent
import android.os.Bundle
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.order.activity.getcustomer.adapter.option.SubProductAdapter
import com.eways.agent.order.activity.getcustomer.viewdto.SubProductViewDTO
import com.eways.agent.utils.customitemdecoration.CustomVerticalItemDecoration
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.proyek.infrastructures.inventory.category.entities.Category
import com.proyek.infrastructures.inventory.item.entities.Grocery
import com.proyek.infrastructures.inventory.item.usecases.GetItemByCategory
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.utils.Authenticated
import kotlinx.android.synthetic.main.activity_getcustomer_titipbelanja_form_subproduct.*

class GetCustomerTitipBelanjaFormSubProductActivity : BaseActivity() {
    private lateinit var category: Category
    private var groceries = ArrayList<Grocery>()

    private lateinit var getItemByCategory: GetItemByCategory

    private lateinit var agent: UserAgent

    private lateinit var customerName: String
    private lateinit var customerAddress: String
    private lateinit var customerPhoneNumber: String
    private lateinit var customerCluster: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getcustomer_titipbelanja_form_subproduct)
        val productType = intent.getStringExtra("product") ?: "Subproduct"
        CustomSupportActionBar.setCustomActionBar(this, productType)

        category = intent.getParcelableExtra("category")

        getItemByCategory = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetItemByCategory::class.java)

        customerName = intent.getStringExtra("customerName")
        customerAddress = intent.getStringExtra("customerAddress")
        customerPhoneNumber = intent.getStringExtra("customerPhoneNumber")
        customerCluster = intent.getStringExtra("customerCluster")

        agent = Authenticated.getUserAgent()

        setSubProductData()
        moveToTitipBelanjaFormConfirmation()
    }

    private fun updateAmount(event: Int, index: Int) {
        if(event==1)
            groceries[index].quantity++
        else {
            if(groceries[index].quantity>0)
                groceries[index].quantity--
        }
    }

    private fun setSubProductData(){
        this@GetCustomerTitipBelanjaFormSubProductActivity.showProgress()
        getItemByCategory.set(category.id!!, this@GetCustomerTitipBelanjaFormSubProductActivity)
        getItemByCategory.get().observe(this, Observer {
            this@GetCustomerTitipBelanjaFormSubProductActivity.dismissProgress()
            val listSubproductViewDTO = ArrayList<SubProductViewDTO>()

            var index = 0

            it.data.forEach {
                groceries.add(index, Grocery(it, 0))
                listSubproductViewDTO.add(
                    SubProductViewDTO(
                        index,
                        it.id,
                        it.imgPath,
                        it.name!!,
                        it.description!!,
                        it.price!!
                    )
                )
                index++
            }

            val subproductAdapter = SubProductAdapter(listSubproductViewDTO) { event, index->
                updateAmount(event, index)
            }
            rvSubproduct.apply {
                layoutManager = LinearLayoutManager(this@GetCustomerTitipBelanjaFormSubProductActivity)
                addItemDecoration(CustomVerticalItemDecoration(15))
                isNestedScrollingEnabled = false
                adapter = subproductAdapter
            }
            svSubproduct.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    subproductAdapter.filter.filter(newText)
                    return false
                }

            })
        })

    }

    private fun moveToTitipBelanjaFormConfirmation(){
        rlCart.setOnClickListener {
            val intent = Intent(this, GetCustomerTitipBelanjaFormConfirmationActivity::class.java)
            intent.putExtra("groceries", groceries)
            intent.putExtra("agent", agent)
            intent.putExtra("category", category)
            intent.putExtra("customerName", customerName)
            intent.putExtra("customerAddress", customerAddress)
            intent.putExtra("customerPhoneNumber", customerPhoneNumber)
            intent.putExtra("customerCluster", customerCluster)
            startActivity(intent)
        }
    }
}