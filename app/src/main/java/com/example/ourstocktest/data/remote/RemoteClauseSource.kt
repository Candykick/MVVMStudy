package com.example.ourstocktest.data.remote

import com.example.ourstocktest.data.service.Api

interface RemoteClauseSource {
    suspend fun showClauseApi(id: Int): String
}

class RemoteClauseSourceImpl(private val service: Api) : RemoteClauseSource {
    //override suspend fun showClauseApi(id: Int) = service.showClauseApi(id)
    override suspend fun showClauseApi(id: Int) : String {
        return ""
    }
}