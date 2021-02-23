package com.example.ourstocktest

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.ourstocktest.data.repository.UserRepository
import com.example.ourstocktest.data.service.API_CONNECT_ERROR_CODE
import com.example.ourstocktest.model.ErrorResponse
import com.example.ourstocktest.model.SingleLiveEvent
import com.example.ourstocktest.model.UserLoginResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/** 대한민국의 휴대전화 번호는 3-3-4(최소 10자) ~ 3-4-4(최대 11자)로 구성된다.
 * (해외 번호까지 포함하면 최대 15자까지도 가능하다고 함.)
 * 인터넷전화 등의 변수도 고려해서, 9자~12자 정도로 생각 중.
 */

class ViewModel(private val repository: UserRepository): ViewModel() {
    val phone = ObservableField<String>()
    val loading = SingleLiveEvent<Boolean>()
    val response = SingleLiveEvent<UserLoginResponse>()
    val error = SingleLiveEvent<ErrorResponse>()

    //val loading = MutableLiveData<Boolean>()
    //val response = MutableLiveData<String>("로그인 안 함")
    /*val response2 = liveData(Dispatchers.IO) {
        if(phone.get() != null && phone.get() != "") {
            loading.postValue(true)
            emit(repository.login(phone.get()!!))
            loading.postValue(false)
        } else
            loading.postValue(false)
    }*/

    fun onLoginButtonClick() {
        if(phone.get() != null && phone.get() != "") {
            loading.postValue(true)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    response.postValue(repository.login(phone.get()!!))
                    loading.postValue(false)
                    /*val result = repository.login(phone.get()!!)
                    if(result.isSucceed) {
                        response.postValue(result.contents!!)
                        loading.postValue(false)
                    } else {
                        error.postValue(result.error!!)
                        loading.postValue(false)
                    }*/
                } catch (e: HttpException) {
                    /** e.response()로는 작업이 불가능. 아래와 같은 형태로 값이 넘어오며, 이는 API에서 오류 발생 시 리턴하는 JSON과는 다르다.
                     *  Response{protocol=http/1.1, code=404, message=, url=http://service.ourstock.ga:9090/login} */
                    val errorResponse: ErrorResponse = if(e.code() == 404) {
                        // 해당하는 유저가 없는 경우
                        ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), e.code(), "No User", "Http Error : No User", "/login")
                    } else if(e.localizedMessage != null) {
                        ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), e.code(), e.localizedMessage!!, "Http Error : " + e.localizedMessage, "/login")
                    } else {
                        ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), e.code(), "Undefined", "Http Error : Undefined Error.", "/login")
                    }
                    error.postValue(errorResponse)
                    loading.postValue(false)
                } catch (e: Exception) {
                    val errorResponse: ErrorResponse

                    if(e.localizedMessage != null) {
                        errorResponse = ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), API_CONNECT_ERROR_CODE, e.localizedMessage!!, "Error : " + e.localizedMessage, "/login")
                    } else {
                        errorResponse = ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), API_CONNECT_ERROR_CODE, "Undefined", "Error : Undefined Error.", "/login")
                    }
                    error.postValue(errorResponse)
                    loading.postValue(false)
                }
            }
        } else
            loading.postValue(false)
    }
}