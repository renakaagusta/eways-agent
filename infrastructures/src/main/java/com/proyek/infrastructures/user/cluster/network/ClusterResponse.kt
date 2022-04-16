package com.proyek.infrastructures.user.cluster.network

import com.proyek.infrastructures.user.cluster.entities.Cluster
import com.proyek.infrastructures.user.agent.entities.Error

data class ClusterResponse (
   val errors: Error,
   val message: String,
   val data: List<Cluster>
)