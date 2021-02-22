package com.example.ourstocktest.model

import com.google.gson.annotations.SerializedName

/** 유저 관련 API에서 사용하는 여러 가지 Data Class */

data class UserRegisterRequest(
    @SerializedName("callNumber") val callNumber: String,
    @SerializedName("telecom") val telecom: String,
    @SerializedName("username") val username: String,
    @SerializedName("residentRegistrationNumberFront") val residentRegistrationNumberFront: String,
    @SerializedName("residentRegistrationNumberBack") val residentRegistrationNumberBack: String
)

data class UserRegisterResponse(
    @SerializedName("accountBank") val accountBank: String,
    @SerializedName("accountName") val accountName: String,
    @SerializedName("accountNumber") val accountNumber: String,
    @SerializedName("additionalAddress") val additionalAddress: String,
    @SerializedName("alarm") val alarm: Int,
    @SerializedName("applicationVersion") val applicationVersion: String,
    @SerializedName("buildingName") val buildingName: String,
    @SerializedName("callNumber") val callNumber: String,
    @SerializedName("cityName") val cityName: String,
    @SerializedName("deposit") val deposit: Int,
    @SerializedName("deviceManufacturer") val deviceManufacturer: String,
    @SerializedName("deviceModel") val deviceModel: String,
    @SerializedName("districtName") val districtName: String,
    @SerializedName("dongName") val dongName: String,
    @SerializedName("investLimit") val investLimit: Int,
    @SerializedName("jwtToken") val jwtToken: String,
    @SerializedName("lastConnection") val lastConnection: String,
    @SerializedName("mainBuildingNumber") val mainBuildingNumber: String,
    @SerializedName("marketing") val marketing: Int,
    @SerializedName("os") val os: String,
    @SerializedName("postCode") val postCode: String,
    @SerializedName("registerTime") val registerTime: String,
    @SerializedName("residentRegistrationNumberBack") val residentRegistrationNumberBack: String,
    @SerializedName("residentRegistrationNumberFront") val residentRegistrationNumberFront: String,
    @SerializedName("roadName") val roadName: String,
    @SerializedName("subBuildingNumber") val subBuildingNumber: String,
    @SerializedName("telecom") val telecom: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("username") val username: String
)

data class UserLoginRequest(
    @SerializedName("callNumber") val callNumber: String
)

data class UserLoginResponse(
    val accountBank: String,
    val accountName: String,
    val accountNumber: String,
    val additionalAddress: String,
    val alarm: Int,
    val applicationVersion: String,
    val buildingName: String,
    val callNumber: String,
    val cityName: String,
    val deposit: Int,
    val deviceManufacturer: String,
    val deviceModel: String,
    val districtName: String,
    val dongName: String,
    val investLimit: Int,
    val jwtToken: String,
    val lastConnection: String,
    val mainBuildingNumber: String,
    val marketing: Int,
    val os: String,
    val postCode: String,
    val registerTime: String,
    val residentRegistrationNumberBack: String,
    val residentRegistrationNumberFront: String,
    val roadName: String,
    val subBuildingNumber: String,
    val telecom: String,
    val userId: Int,
    val username: String
)

data class UserFcmSaveRequest(
    @SerializedName("userId") val userId: Int,
    @SerializedName("fcmToken") val fcmToken: String
)

data class UserFcmSaveResponse(
    @SerializedName("fcmId") val fcmId: Int,
    @SerializedName("fcmToken") val fcmToken: String,
    @SerializedName("lastChecktime") val lastChecktime: String,
    @SerializedName("registerTime") val registerTime: String,
    @SerializedName("userId") val userId: Int
)

data class UserAlarmUpdateRequest(
    @SerializedName("userId") val userId: Int,
    @SerializedName("alarm") val alarm: Int,
    @SerializedName("marketing") val marketing: Int
)

data class UserAlarmUpdateResponse(
    @SerializedName("accountBank") val accountBank: String,
    @SerializedName("accountName") val accountName: String,
    @SerializedName("accountNumber") val accountNumber: String,
    @SerializedName("additionalAddress") val additionalAddress: String,
    @SerializedName("alarm") val alarm: Int,
    @SerializedName("applicationVersion") val applicationVersion: String,
    @SerializedName("buildingName") val buildingName: String,
    @SerializedName("callNumber") val callNumber: String,
    @SerializedName("cityName") val cityName: String,
    @SerializedName("deposit") val deposit: Int,
    @SerializedName("deviceManufacturer") val deviceManufacturer: String,
    @SerializedName("deviceModel") val deviceModel: String,
    @SerializedName("districtName") val districtName: String,
    @SerializedName("dongName") val dongName: String,
    @SerializedName("investLimit") val investLimit: Int,
    @SerializedName("jwtToken") val jwtToken: String,
    @SerializedName("lastConnection") val lastConnection: String,
    @SerializedName("mainBuildingNumber") val mainBuildingNumber: String,
    @SerializedName("marketing") val marketing: Int,
    @SerializedName("os") val os: String,
    @SerializedName("postCode") val postCode: String,
    @SerializedName("registerTime") val registerTime: String,
    @SerializedName("residentRegistrationNumberBack") val residentRegistrationNumberBack: String,
    @SerializedName("residentRegistrationNumberFront") val residentRegistrationNumberFront: String,
    @SerializedName("roadName") val roadName: String,
    @SerializedName("subBuildingNumber") val subBuildingNumber: String,
    @SerializedName("telecom") val telecom: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("username") val username: String
)