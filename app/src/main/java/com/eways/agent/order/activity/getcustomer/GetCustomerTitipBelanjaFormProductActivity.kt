package com.eways.agent.order.activity.getcustomer

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.order.activity.getcustomer.adapter.ProductAdapter
import com.eways.agent.order.activity.getcustomer.viewdto.ProductViewDTO
import com.eways.agent.utils.customitemdecoration.CustomDividerItemDecoration
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.proyek.infrastructures.inventory.category.entities.Category
import com.proyek.infrastructures.inventory.category.usecases.GetCategoryList
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.utils.Authenticated
import kotlinx.android.synthetic.main.activity_getcustomer_titipbelanja_form_product.*
import java.util.ArrayList

class GetCustomerTitipBelanjaFormProductActivity : BaseActivity(){
    private lateinit var getCategoryList: GetCategoryList

    private lateinit var agent: UserAgent
    private var listCategory = ArrayList<Category>()

    private lateinit var customerName: String
    private lateinit var customerAddress: String
    private lateinit var customerPhoneNumber: String
    private lateinit var customerCluster: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_getcustomer_titipbelanja_form_product)
        CustomSupportActionBar.setCustomActionBar(this, "Titip Belanja")

        getCategoryList = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetCategoryList::class.java)

        customerName = intent.getStringExtra("customerName")
        customerAddress = intent.getStringExtra("customerAddress")
        customerPhoneNumber = intent.getStringExtra("customerPhoneNumber")
        customerCluster = intent.getStringExtra("customerCluster")

        agent = Authenticated.getUserAgent()

        setProductData()
    }

    private fun setProductData(){
        getCategoryList.set(this@GetCustomerTitipBelanjaFormProductActivity)
        getCategoryList.get().observe(this, Observer {
            val listProductViewDTO = ArrayList<ProductViewDTO>()

            listCategory.addAll(it.data)

            it.data.forEach {
                listProductViewDTO.add(
                    ProductViewDTO(
                        it.id!!,
                        it.name!!,
                        ""
                    )
                )
            }

            val productAdapter = ProductAdapter(listProductViewDTO, customerName, customerPhoneNumber, customerAddress, customerCluster, listCategory)
            rvProduct.apply {
                layoutManager = LinearLayoutManager(this@GetCustomerTitipBelanjaFormProductActivity)
                addItemDecoration(CustomDividerItemDecoration(ContextCompat.getDrawable(this@GetCustomerTitipBelanjaFormProductActivity, R.drawable.divider_line)!!))
                isNestedScrollingEnabled = false
                adapter = productAdapter
            }
        })
    }
}