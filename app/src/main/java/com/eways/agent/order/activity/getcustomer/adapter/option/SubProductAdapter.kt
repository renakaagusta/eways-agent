package com.eways.agent.order.activity.getcustomer.adapter.option

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.eways.agent.R
import com.eways.agent.order.activity.getcustomer.viewdto.SubProductViewDTO
import com.eways.agent.utils.MoneyUtils
import kotlinx.android.synthetic.main.row_subproduct.view.*
import java.util.*
import kotlin.collections.ArrayList


class SubProductAdapter(private val subProducts : MutableList<SubProductViewDTO>, val itemOnClick : (Int, Int) -> Unit) : RecyclerView.Adapter<SubProductAdapter.SubproductViewHolder>(), Filterable {
    private var itemListFull:List<SubProductViewDTO> = subProducts
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubproductViewHolder {
        return SubproductViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_subproduct, parent, false))
    }

    override fun getItemCount(): Int = subProducts.size

    override fun onBindViewHolder(holder: SubproductViewHolder, position: Int) {
        holder.bindSubproduct(subProducts[position])
    }

    inner class SubproductViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bindSubproduct(subProductViewDTO: SubProductViewDTO){
            itemView.apply {
                tvSubproductName.text = subProductViewDTO.subproductName
                tvSubproductPrice.text = MoneyUtils.getAmountString(subProductViewDTO.subproductPrice)
                ivSubproduct.setImageResource(R.drawable.ic_product)

                if(subProductViewDTO.subproductAmount == 0){
                    tvAdd.isVisible =true
                    llAmount.isVisible = false
                }

                tvAdd.setOnClickListener { 
                    subProductViewDTO.subproductAmount += 1
                    tvSubproductAmount.text = subProductViewDTO.subproductAmount.toString()
                    itemOnClick(1, subProductViewDTO.id!!)
                    tvAdd.isVisible = false
                    llAmount.isVisible = true
                }
                bAdd.setOnClickListener {
                    subProductViewDTO.subproductAmount += 1
                    tvSubproductAmount.text = subProductViewDTO.subproductAmount.toString()
                    itemOnClick(1, subProductViewDTO.id!!)
                }
                bMinus.setOnClickListener {
                    subProductViewDTO.subproductAmount -= 1
                    tvSubproductAmount.text = subProductViewDTO.subproductAmount.toString()
                    itemOnClick(-1, subProductViewDTO.id!!)
                    if(subProductViewDTO.subproductAmount==0){
                        tvAdd.isVisible = true
                        llAmount.isVisible = false
                    }
                }
            }
        }
    }

    override fun getFilter(): Filter {
        return exampleFilter
    }
    private val exampleFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: ArrayList<SubProductViewDTO> = ArrayList()
            if (constraint.isEmpty()) {
                filteredList.addAll(itemListFull)
            } else {
                val filterPattern =
                    constraint.toString().toLowerCase(Locale.ROOT).trim { it <= ' ' }
                for (item in itemListFull) {
                    if (item.subproductName.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(
            constraint: CharSequence,
            results: FilterResults
        ) {
            subProducts.clear()
            subProducts.addAll(results.values as java.util.ArrayList<SubProductViewDTO>)
            notifyDataSetChanged()
        }

    }
}