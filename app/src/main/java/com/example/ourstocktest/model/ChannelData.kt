package com.example.ourstocktest.model

import com.google.gson.annotations.SerializedName

/** 메인 화면의 채널 리스트에 쓰이는 model */
class ChannelDataList : ArrayList<ChannelData>()

data class ChannelData(
    @SerializedName("image") val image: String,
    @SerializedName("title") val title: String,
    @SerializedName("stock") val stock: Int,
    @SerializedName("isUp") val isUp: Boolean,
    @SerializedName("variation") val variation: Int,
    @SerializedName("percent") val percent: Double
)



/** 채널 관련 API들을 호출할 때 사용하는 model */
data class ChannelIdRequest(
    @SerializedName("id") val id: String
)



/** 특정 채널의 정보를 가져올 때 사용하는 model */
data class ChannelInfo(
        val currentPrice: Int,
        val currentPricePercent: Double,
        val date: String,
        val highPrice: Int,
        val highPricePercent: Double,
        val hits: Int,
        val id: String,
        val introductionVideo: String,
        val lowPrice: Int,
        val lowPricePercent: Double,
        val majorShareholder: String,
        val marketCapitalization: Int,
        val subscribers: Int,
        val transactionPrice: Int,
        val videoNum: Int,
        val volume: Int
)



/** 특정 채널의 차트에 대한 정보를 가져올 때 사용하는 model */
class ChannelChartData : ArrayList<ChannelChartItem>()

data class ChannelChartItem(
        val dateTime: String,
        val isUp: Boolean,
        val stock: Int,
        val variation: Int,
        val volume: Int
)
