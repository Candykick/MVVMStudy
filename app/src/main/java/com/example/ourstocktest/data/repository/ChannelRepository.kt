package com.example.ourstocktest.data.repository

import android.text.TextUtils
import com.example.ourstocktest.data.remote.RemoteChannelSource
import com.example.ourstocktest.model.*
import com.google.gson.Gson
import retrofit2.Response

interface ChannelRepository {
    suspend fun getChannelInfo(id: String) : ApiResponse<ChannelInfo>
    suspend fun getLiveChart(id: String) : ApiResponse<ChannelChartData>
    suspend fun getDayChart(id: String) : ApiResponse<ChannelChartData>
}

class ChannelRepositoryImpl(private val channelRemoteSource: RemoteChannelSource) : ChannelRepository {

    override suspend fun getChannelInfo(id: String): ApiResponse<ChannelInfo> {
        val apiResult: Response<String> = channelRemoteSource.channelInfoApi(ChannelIdRequest(id))

        if (apiResult.code() == 200) {
            return ApiResponse(true, Gson().fromJson(apiResult.body()!!, ChannelInfo::class.java), null)
        } else if (!TextUtils.isEmpty(apiResult.body())) {
            return ApiResponse(false, null, Gson().fromJson(apiResult.body()!!, ErrorResponse::class.java))
        } else {
            return ApiResponse(false, null, makeErrorResponseFromStatusCode(apiResult.code(), "/channelInfo"))
        }
    }

    override suspend fun getLiveChart(id: String): ApiResponse<ChannelChartData> {
        val apiResult: Response<String> = channelRemoteSource.channelLiveChartApi(ChannelIdRequest(id))

        if (apiResult.code() == 200) {
            return ApiResponse(true, Gson().fromJson(apiResult.body()!!, ChannelChartData::class.java), null)
        } else if (!TextUtils.isEmpty(apiResult.body())) {
            return ApiResponse(false, null, Gson().fromJson(apiResult.body()!!, ErrorResponse::class.java))
        } else {
            return ApiResponse(false, null, makeErrorResponseFromStatusCode(apiResult.code(), "/channelInfo"))
        }
    }

    override suspend fun getDayChart(id: String): ApiResponse<ChannelChartData> {
        val apiResult: Response<String> = channelRemoteSource.channelDayChartApi(ChannelIdRequest(id))

        if (apiResult.code() == 200) {
            return ApiResponse(true, Gson().fromJson(apiResult.body()!!, ChannelChartData::class.java), null)
        } else if (!TextUtils.isEmpty(apiResult.body())) {
            return ApiResponse(false, null, Gson().fromJson(apiResult.body()!!, ErrorResponse::class.java))
        } else {
            return ApiResponse(false, null, makeErrorResponseFromStatusCode(apiResult.code(), "/channelInfo"))
        }
    }
}