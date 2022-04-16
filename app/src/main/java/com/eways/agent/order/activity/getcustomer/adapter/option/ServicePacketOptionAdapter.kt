package com.eways.agent.order.activity.getcustomer.adapter.option

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.eways.agent.R
import com.eways.agent.order.activity.getcustomer.viewdto.InternetServiceOptionViewDTO
import kotlinx.android.synthetic.main.row_option_basic.view.*
import java.util.*
import kotlin.collections.ArrayList

class ServicePacketOptionAdapter(private var itemList: ArrayList<InternetServiceOptionViewDTO>): RecyclerView.Adapter<ServicePacketOptionAdapter.OptionViewHolder>(), Filterable {
    private var itemListFull:List<InternetServiceOptionViewDTO> = itemList
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        return OptionViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_option_basic, parent, false))
    }

    override fun getItemCount(): Int =itemList.size

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.bindOption(itemList[position])
    }

    inner class OptionViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        fun bindOption(string: InternetServiceOptionViewDTO) {
            itemView.apply {
                tvOption.text = string.name
                setOnClickListener {
                    val intent = Intent()
                    intent.putExtra("service_packet", string.id)
                    (this.context as Activity).setResult(Activity.RESULT_OK, intent)
                    (this.context as Activity).finish()
                }
            }
        }
    }

    override fun getFilter(): Filter {
        return exampleFilter
    }
    private val exampleFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: ArrayList<InternetServiceOptionViewDTO> = ArrayList()
            if (constraint.isEmpty()) {
                filteredList.addAll(itemListFull)
            } else {
                val filterPattern =
                    constraint.toString().toLowerCase(Locale.ROOT).trim { it <= ' ' }
                for (item in itemListFull) {
                    if (item.name.toLowerCase(Locale.ROOT).contains(filterPattern)) {
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
            itemList.clear()
            itemList.addAll(results.values as java.util.ArrayList<InternetServiceOptionViewDTO>)
            notifyDataSetChanged()
        }

    }
}