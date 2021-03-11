package com.example.ourstocktest.data.remote

import com.example.ourstocktest.data.service.Api
import com.example.ourstocktest.model.ChannelDataList
import retrofit2.Response

interface RemoteMainSource {
    suspend fun channelListApi(): Response<String>
}

class RemoteMainSourceImpl(private val service: Api) : RemoteMainSource {
    override suspend fun channelListApi() = service.channelListApi()
}