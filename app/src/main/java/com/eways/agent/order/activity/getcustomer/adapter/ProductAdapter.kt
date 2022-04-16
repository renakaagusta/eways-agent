package com.eways.agent.order.activity.getcustomer.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eways.agent.R
import com.eways.agent.order.activity.getcustomer.GetCustomerTitipBelanjaFormSubProductActivity
import com.eways.agent.order.activity.getcustomer.viewdto.ProductViewDTO
import com.proyek.infrastructures.inventory.category.entities.Category
import kotlinx.android.synthetic.main.row_product.view.*

class ProductAdapter(private val products : List<ProductViewDTO>, customerName: String, customerPhoneNumber: String, customerAddress: String, customerCluster: String, listCategory: ArrayList<Category>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    val customerName = customerName
    val customerPhoneNumber = customerPhoneNumber
    val customerAddress = customerAddress
    val customerCluster = customerCluster
    val listCategory = listCategory

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_product, parent, false))
    }

    override fun getItemCount(): Int =products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindProduct(products[position], customerName, customerPhoneNumber, customerAddress, customerCluster, listCategory[position])
    }

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bindProduct(productViewDTO: ProductViewDTO, customerName: String, customerPhoneNumber: String, customerAddress: String, customerCluster: String, category: Category){
            itemView.apply {
                tvProduct.text = productViewDTO.productName
                ivProduct.setImageResource(R.drawable.ic_product)

                setOnClickListener {
                    val intent = Intent(this.context, GetCustomerTitipBelanjaFormSubProductActivity::class.java)
                    intent.putExtra("category", category)
                    intent.putExtra("customerName", customerName)
                    intent.putExtra("customerAddress", customerAddress)
                    intent.putExtra("customerPhoneNumber", customerPhoneNumber)
                    intent.putExtra("customerCluster", customerCluster)
                    this.context.startActivity(intent)
                }
            }
        }
    }
}