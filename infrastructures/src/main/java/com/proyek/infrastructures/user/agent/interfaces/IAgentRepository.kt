package com.proyek.infrastructures.user.agent.interfaces

interface IAgentRepository {
    fun find(): Int
    fun save(): Int
}