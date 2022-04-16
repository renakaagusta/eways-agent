package com.eways.agent.order.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eways.agent.R
import com.eways.agent.order.activity.getcustomer.GetCustomerTitipBelanjaFormSubProductActivity
import com.eways.agent.order.viewdto.ProductViewDTO
import kotlinx.android.synthetic.main.row_product.view.*

class ProductAdapter(private val products : List<ProductViewDTO>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_product, parent, false))
    }

    override fun getItemCount(): Int =products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bindProduct(products[position])
    }

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bindProduct(productViewDTO: ProductViewDTO){
            itemView.apply {
                tvProduct.text = productViewDTO.productName
                ivProduct.setImageResource(R.drawable.ic_product)

                setOnClickListener {
                    val intent = Intent(this.context, GetCustomerTitipBelanjaFormSubProductActivity::class.java)
                    intent.putExtra("product",productViewDTO.productName)
                    this.context.startActivity(intent)
                }
            }
        }
    }
}