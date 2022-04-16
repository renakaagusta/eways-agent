package com.eways.agent.kabarcluster.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eways.agent.kabarcluster.viewdto.KabarClusterCommentViewDTO
import com.eways.agent.R
import com.proyek.infrastructures.user.user.entities.User
import com.proyek.infrastructures.user.user.usecases.GetUserDetail
import kotlinx.android.synthetic.main.row_kabarcluster.view.*


class KabarClusterCommentAdapter(private val kabarClusters : List<KabarClusterCommentViewDTO>, private val listUser: ArrayList<User>, private val storeOwner: ViewModelStoreOwner, private val owner: LifecycleOwner) : RecyclerView.Adapter<KabarClusterCommentAdapter.KabarClusterCommentViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KabarClusterCommentViewHolder {
        return KabarClusterCommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_kabarcluster, parent, false), storeOwner, owner)
    }

    override fun getItemCount(): Int =kabarClusters.size

    override fun onBindViewHolder(holder: KabarClusterCommentViewHolder, position: Int) {
        holder.bindKabarCluster(kabarClusters[position], listUser[position])
    }

    inner class KabarClusterCommentViewHolder(view: View, storeOwner: ViewModelStoreOwner, owner: LifecycleOwner) : RecyclerView.ViewHolder(view){
        val getUserDetail: GetUserDetail = ViewModelProvider(storeOwner, ViewModelProvider.NewInstanceFactory()).get(GetUserDetail::class.java)
        val view = view

        fun bindKabarCluster(kabarClusterCommentViewDTO: KabarClusterCommentViewDTO, user: User){

                    itemView.tvCreator.text = user.username
                        if (user.imagePath != null) Glide.with(itemView)
                            .load("http://13.229.200.77:8001/storage/${user.imagePath}")
                            .into(itemView.imgProfile)

                    itemView.apply {
                        tvContent.text = kabarClusterCommentViewDTO.content
                        tvCreatedAt.text = kabarClusterCommentViewDTO.createdAt.getLocalizeDateString()
                        llCommentCount.isVisible = false
                    }
            }
        }
    }
