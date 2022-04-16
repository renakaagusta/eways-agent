package com.eways.agent.order.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eways.agent.R
import com.eways.agent.order.viewdto.TransactionViewDTO
import com.eways.agent.utils.MoneyUtils
import kotlinx.android.synthetic.main.row_titipbelanja_transaction.view.*

class TitipBelanjaTransactionAdapter(private val products : List<TransactionViewDTO>) : RecyclerView.Adapter<TitipBelanjaTransactionAdapter.SubproductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubproductViewHolder {
        return SubproductViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_titipbelanja_transaction, parent, false))
    }

    override fun getItemCount(): Int =products.size

    override fun onBindViewHolder(holder: SubproductViewHolder, position: Int) {
        holder.bindSubProduct(products[position])
    }

    inner class SubproductViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bindSubProduct(transactionViewDTO: TransactionViewDTO){
            itemView.apply {
                tvSubproductName.text = transactionViewDTO.subproductName
                tvSubproductPrice.text = "Harga Satuan : "+ MoneyUtils.getAmountString(transactionViewDTO.subproductPrice)
                tvSubproductAmount.text = transactionViewDTO.suProductAmount.toString() + " item"
                tvTotal.text = (transactionViewDTO.subproductPrice *transactionViewDTO.suProductAmount).toString()
                ivSubproduct.setImageResource(R.drawable.ic_product)
            }
        }
    }
}