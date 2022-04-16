package com.eways.agent.kabarcluster.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eways.agent.R
import com.eways.agent.kabarcluster.activity.KabarClusterReadActivity
import com.eways.agent.kabarcluster.viewdto.KabarClusterPostViewDTO
import kotlinx.android.synthetic.main.row_kabarcluster.view.*

class KabarClusterPostAdapter(private val kabarClusters: ArrayList<KabarClusterPostViewDTO>, val clusterId: String, val userId: String) : RecyclerView.Adapter<KabarClusterPostAdapter.KabarClusterPostViewHolder>(){

    fun clear() {
        val size: Int = kabarClusters.size
        kabarClusters.clear()
        notifyItemRangeRemoved(0, size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KabarClusterPostViewHolder {
        return KabarClusterPostViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_kabarcluster, parent, false))
    }

    override fun getItemCount(): Int = kabarClusters.size

    override fun onBindViewHolder(holder: KabarClusterPostViewHolder, position: Int) {
        holder.bindKabarCluster(kabarClusters[position])
    }

    inner class KabarClusterPostViewHolder(view: View) : RecyclerView.ViewHolder(view){

        fun bindKabarCluster(kabarClusterPostViewDTO: KabarClusterPostViewDTO){

            itemView.apply {
                if(kabarClusterPostViewDTO.imagePath!="")
                    Glide.with(this)
                        .load("http://13.229.200.77:8001/storage/${kabarClusterPostViewDTO.imagePath}")
                        .into(imgProfile)
                tvCreator.text = kabarClusterPostViewDTO.creator
                tvContent.text = kabarClusterPostViewDTO.content
                tvCreatedAt.text = kabarClusterPostViewDTO.createdAt.getLocalizeDateString()
                tvCommentCount.text = kabarClusterPostViewDTO.commentCount.toString() + " komentar"
                if(kabarClusterPostViewDTO.pinned){
                    tvPinnedPost.isVisible = true
                }

                setOnClickListener {
                    val intent = Intent(this.context, KabarClusterReadActivity::class.java)
                    intent.putExtra("postId", kabarClusterPostViewDTO.id)
                    intent.putExtra("userId", userId)
                    intent.putExtra("clusterId", clusterId)
                    this.context.startActivity(intent)
                }
            }
        }
    }
}