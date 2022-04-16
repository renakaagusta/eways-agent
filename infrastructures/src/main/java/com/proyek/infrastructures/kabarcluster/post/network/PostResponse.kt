package com.proyek.infrastructures.kabarcluster.post.network

import com.proyek.infrastructures.kabarcluster.entities.Post
import com.proyek.infrastructures.user.agent.entities.Error

data class PostResponse(
    val errors: Error,
    val message: String,
    val data: ArrayList<Post>
)