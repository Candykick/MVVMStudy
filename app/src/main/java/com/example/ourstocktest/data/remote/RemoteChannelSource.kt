package com.example.ourstocktest.data.remote

import com.example.ourstocktest.data.service.Api
import com.example.ourstocktest.model.ChannelIdRequest
import retrofit2.Response

interface RemoteChannelSource {
    suspend fun channelInfoApi(channelId: ChannelIdRequest): Response<String>
    suspend fun channelLiveChartApi(channelId: ChannelIdRequest): Response<String>
    suspend fun channelDayChartApi(channelId: ChannelIdRequest): Response<String>
}

class RemoteChannelSourceImpl(private val service: Api) : RemoteChannelSource {
    override suspend fun channelInfoApi(channelId: ChannelIdRequest) = service.channelInfoApi(channelId)
    override suspend fun channelLiveChartApi(channelId: ChannelIdRequest) = service.channelLiveChartApi(channelId)
    override suspend fun channelDayChartApi(channelId: ChannelIdRequest) = service.channelDayChartApi(channelId)
}