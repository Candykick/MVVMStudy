package com.example.ourstocktest.data.repository

import android.text.TextUtils
import android.util.Log
import com.example.ourstocktest.data.remote.RemoteMainSource
import com.example.ourstocktest.data.service.API_CALL_ERROR_MESSAGE
import com.example.ourstocktest.model.*
import com.google.gson.Gson
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

interface MainRepository {
    suspend fun getChannelList() : ApiResponse<ChannelDataList>
}

class MainRepositoryImpl(private val remoteMainSource: RemoteMainSource) : MainRepository {
    override suspend fun getChannelList(): ApiResponse<ChannelDataList> {
        val apiResult: Response<String> = remoteMainSource.channelListApi()

        if (apiResult.code() == 200) {
            return ApiResponse(true, Gson().fromJson(apiResult.body()!!, ChannelDataList::class.java), null)
        } else if (!TextUtils.isEmpty(apiResult.body())) {
            return ApiResponse(false, null, Gson().fromJson(apiResult.body()!!, ErrorResponse::class.java))
        } else {
            return ApiResponse(false, null, makeErrorResponseFromStatusCode(apiResult.code(), "/channelList"))
        }
    }
}