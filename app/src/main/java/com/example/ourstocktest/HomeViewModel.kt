package com.example.ourstocktest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.ourstocktest.data.repository.MainRepository
import com.example.ourstocktest.data.service.API_CONNECT_ERROR_CODE
import com.example.ourstocktest.data.service.NO_INTERNET_ERROR_CODE
import com.example.ourstocktest.model.ChannelData
import com.example.ourstocktest.model.ErrorResponse
import com.example.ourstocktest.model.SingleLiveEvent
import com.example.ourstocktest.model.makeErrorResponseFromStatusCode
import com.example.ourstocktest.util.NetworkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeViewModel(private val repository: MainRepository): ViewModel() {
    // Youtube Channel List
    val channelList = MutableLiveData<ArrayList<ChannelData>>()
    val error = SingleLiveEvent<ErrorResponse>()

    init {
        refreshChannelList()
    }

    fun refreshChannelList() {
        if(NetworkManager().checkNetworkState()) {
            CoroutineScope(Dispatchers.IO).launch {
                val apiRespose = repository.getChannelList()

                if (apiRespose.isSucceed)
                    channelList.postValue(apiRespose.contents!!)
                else {
                    error.postValue(apiRespose.error!!)
                }
            }
        } else {
            error.postValue(makeErrorResponseFromStatusCode(NO_INTERNET_ERROR_CODE, ""))
        }
    }
}