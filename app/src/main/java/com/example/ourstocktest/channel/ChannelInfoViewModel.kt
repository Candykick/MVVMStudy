package com.example.ourstocktest.channel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ourstocktest.data.repository.ChannelRepository
import com.example.ourstocktest.data.service.NO_INTERNET_ERROR_CODE
import com.example.ourstocktest.model.*
import com.example.ourstocktest.util.NetworkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** ChannelInfoViewModel은 3개의 Fragment(정보, 실시간, 일별)를 관리하는 ViewModel이다.
 *  따라서, 각 탭에 들어가는 데이터들은 각각의 탭(Fragment)에서 사용하며,
 *  정보 갱신 관련 SingleLiveEvent는 전체 Activity에서 사용한다. (오류 메세지/Loading Dialog가 여러 번 뜨는 현상을 막기 위함.)
 */

class ChannelInfoViewModel(private val channelId: String, private val repository: ChannelRepository): ViewModel() {
    // 각 탭에 들어가는 데이터들
    val channelInfo = MutableLiveData<ChannelInfo>()
    val channelLiveChart = MutableLiveData<ArrayList<ChannelChartItem>>()
    val channelDayChart = MutableLiveData<ArrayList<ChannelChartItem>>()
    // 정보 갱신 관련 SingleLiveEvent
    val loading = SingleLiveEvent<Boolean>()
    val error = SingleLiveEvent<ErrorResponse>()

    init {
        settingAllData()
        loading.postValue(true)
    }

    /** 전체 데이터를 Setting(Init) */
    fun settingAllData() {
        if(NetworkManager().checkNetworkState()) {
            CoroutineScope(Dispatchers.IO).launch {
                val apiResponse1 = repository.getChannelInfo(channelId)
                val apiResponse2 = repository.getLiveChart(channelId)
                val apiResponse3 = repository.getDayChart(channelId)

                if(apiResponse1.isSucceed && apiResponse2.isSucceed && apiResponse3.isSucceed) {
                    channelInfo.postValue(apiResponse1.contents!!)
                    channelLiveChart.postValue(apiResponse2.contents!!)
                    channelDayChart.postValue(apiResponse3.contents!!)
                } else {
                    if(!apiResponse1.isSucceed)
                        error.postValue(apiResponse1.error!!)
                    else if(!apiResponse2.isSucceed)
                        error.postValue(apiResponse2.error!!)
                    else
                        error.postValue(apiResponse3.error!!)
                }

                loading.postValue(false)
            }
        } else {
            error.postValue(makeErrorResponseFromStatusCode(NO_INTERNET_ERROR_CODE, ""))
            loading.postValue(false)
        }
    }

    /** 정보 탭과 일별 차트 탭을 Refresh하는 함수. */
    // 실시간 차트는 Socket 통신을 통해 계속 Refresh되므로 따로 함수가 필요하지 않다.
    fun refreshInfo() {
        if(NetworkManager().checkNetworkState()) {
            CoroutineScope(Dispatchers.IO).launch {
                val apiResponse = repository.getChannelInfo(channelId)

                if(apiResponse.isSucceed)
                    channelInfo.postValue(apiResponse.contents!!)
                else
                    error.postValue(apiResponse.error!!)

                loading.postValue(false)
            }
        } else {
            error.postValue(makeErrorResponseFromStatusCode(NO_INTERNET_ERROR_CODE, ""))
            loading.postValue(false)
        }
    }

    fun refreshDayChart() {
        if(NetworkManager().checkNetworkState()) {
            CoroutineScope(Dispatchers.IO).launch {
                val apiResponse = repository.getDayChart(channelId)

                if(apiResponse.isSucceed)
                    channelDayChart.postValue(apiResponse.contents!!)
                else
                    error.postValue(apiResponse.error!!)

                loading.postValue(false)
            }
        } else {
            error.postValue(makeErrorResponseFromStatusCode(NO_INTERNET_ERROR_CODE, ""))
            loading.postValue(false)
        }
    }
}