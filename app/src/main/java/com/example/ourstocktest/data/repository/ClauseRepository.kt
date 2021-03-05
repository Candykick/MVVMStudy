package com.example.ourstocktest.data.repository

import com.example.ourstocktest.data.remote.RemoteClauseSource

interface ClauseRepository {
    suspend fun showClauseHtml(id: Int): String
}

class ClauseRepositoryImpl(private val remoteClauseSource: RemoteClauseSource) : ClauseRepository {
    override suspend fun showClauseHtml(id: Int): String {
        when(id) {
            1 -> return "<html><head></head><body><b>1번째입니다.</b></body></html>"
            2 -> return "<html><head></head><body><b>2번째다!</b></body></html>"
            3 -> return "<html><head></head><body><b>3번째네?</b></body></html>"
            4 -> return "<html><head></head><body><b>4번째야!!!</b></body></html>"
        }

        return "<html><head></head><body><b>BUG BUG BUG BUG</b></body></html>"
    }
}