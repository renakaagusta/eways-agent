package com.proyek.infrastructures.kabarcluster.comment.network

import com.proyek.infrastructures.kabarcluster.entities.Comment
import com.proyek.infrastructures.user.agent.entities.Error

data class CommentResponse(
    val errors: Error ?= null,
    val message: String ?= null,
    val data: List<Comment> ?= null
)