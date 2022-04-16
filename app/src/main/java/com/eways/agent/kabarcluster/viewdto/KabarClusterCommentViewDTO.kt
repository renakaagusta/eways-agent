package com.eways.agent.kabarcluster.viewdto

import com.eways.agent.utils.date.SLDate

data class KabarClusterCommentViewDTO (
    val id: String,
    val creator : String,
    val content : String,
    val createdAt : SLDate
)