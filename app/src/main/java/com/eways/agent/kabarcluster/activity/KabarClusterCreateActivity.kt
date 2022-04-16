package com.eways.agent.kabarcluster.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.proyek.infrastructures.kabarcluster.post.usecases.CreatePost
import com.proyek.infrastructures.user.agent.entities.UserAgent
import kotlinx.android.synthetic.main.activity_kabarcluster_create.*
import kotlinx.android.synthetic.main.supportactionbar_post.*

class KabarClusterCreateActivity :BaseActivity() {
    private lateinit var createPost: CreatePost
    private lateinit var user: UserAgent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kabarcluster_create)
        CustomSupportActionBar.setCustomActionBarKabarCluster(this, R.layout.supportactionbar_post)
    }

    override fun onStart() {
        super.onStart()

        user = intent.getParcelableExtra("user")

        if(user.imagePath!=null)
            Glide.with(this)
                .load("http://13.229.200.77:8001/storage/${user.imagePath}")
                .into(civUserImage)

        createPost = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(CreatePost::class.java)

        moveToKabarClusterRead()
    }

    private fun moveToKabarClusterRead(){
        tvPost.setOnClickListener {
            createPost.set(user.ID!!, user.cluster?.ID!!, tietPost.text.toString(), this@KabarClusterCreateActivity)
            createPost.get().observe(this, Observer {
                this@KabarClusterCreateActivity.dismissProgress()
                startActivity(Intent(this@KabarClusterCreateActivity, KabarClusterActivity::class.java))
                finish()
            })
        }
    }
}