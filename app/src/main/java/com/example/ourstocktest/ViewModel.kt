package com.example.ourstocktest

import android.app.Activity
import android.content.Context
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ourstocktest.data.repository.*
import com.example.ourstocktest.data.service.API_CONNECT_ERROR_CODE
import com.example.ourstocktest.model.ErrorResponse
import com.example.ourstocktest.model.SingleLiveEvent
import com.example.ourstocktest.model.UserLoginResponse
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.koinApplication
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent.inject
import retrofit2.HttpException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

/** 대한민국의 휴대전화 번호는 3-3-4(최소 10자) ~ 3-4-4(최대 11자)로 구성된다.
 * (해외 번호까지 포함하면 최대 15자까지도 가능하다고 함.)
 * 인터넷전화 등의 변수도 고려해서, 9자~12자 정도로 생각 중.
 */

/** Signup Status를 ViewModel에서 관리하는 이유
 *  : 이 부분에 대해서 10분 정도 고민했다. SignupActivity에서 다루는 게 맞는지, ViewModel에서 다루는 게 맞는지 고민이었는데, ViewModel에서 다루는 게 맞다는 결론이 나왔다.
 *    그 이유는 다음과 같다.
 *    1. 다음/회원가입 버튼의 Event를 관리하는 주체가 ViewModel이다.
 *       (버튼 클릭에 맞춰서 Signup Status를 하나씩 증가시키고, 마지막에는 API를 호출해야 하므로 ViewModel에서만 작업해야 한다.)
 *    2. 만약 Signup Activity의 레이아웃에 변경이 생긴다면, ViewModel에서 받는 status에 따른 view의 변경 부분만 신경쓰면 된다.
 *       (굳이 status 자체를 뜯을 이유는 없다는 말. 왜냐, status는 API에 의존하는 형태니까.)
 */

// Signup Activity의 상태 : 회원가입 시작 ~ 모든 폼 입력 완료
const val STATE_SIGNUP_INITIALIZED = 1
const val STATE_SIGNUP_INPUT_NAME = 2
const val STATE_SIGNUP_INPUT_NUMBER = 3
const val STATE_SIGNUP_INPUT_TELECOM = 4
const val STATE_SIGNUP_INPUT_PHONE = 5
const val STATE_SIGNUP_ALL_INFO_INSERTED = 6
// Signup Activity의 상태 : 인증 코드 전송 ~ 로그인 성공
const val STATE_SIGNUP_CODE_SENT = 7
const val STATE_SIGNUP_VERIFY_FAILED = 8
const val STATE_SIGNUP_VERIFY_SUCCESS = 9
const val STATE_SIGNIN_FAILED = 10
const val STATE_SIGNIN_SUCCESS = 11

class ViewModel(private val repository: UserRepository, val activity: Activity): ViewModel() {
    // Signup Activity의 상태
    val status = SingleLiveEvent<Int>()

    // 회원가입 시 필요한 값들
    // (값의 변경이 ViewModel에서만 일어나지 않기 때문에 private로 숨기지 않음.)
    val username = MutableLiveData<String>("")
    val telecom = MutableLiveData<String>("")
    val phone = MutableLiveData<String>("")
    val residentNumberFront = MutableLiveData<String>("")
    val residentNumberBack = MutableLiveData<String>("")

    // 인증번호
    val verification = MutableLiveData<String>("")
    val time = SingleLiveEvent<String>()
    val timeEnd = SingleLiveEvent<Boolean>()
    // 타이머
    lateinit var timer: TimeoutTimer

    // 회원가입 API 실행 후 반환하는 값들 + 로딩 체크
    val loading = SingleLiveEvent<Boolean>()
    val response = SingleLiveEvent<UserLoginResponse>()
    val error = SingleLiveEvent<ErrorResponse>()

    // 인증 상태인지 아닌지 체크.
    private var verificationInProgress = false
    // verification ID and resending token
    private lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    /** 전화번호 인증 요청에 대한 콜백 */
    var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        // 즉시 인증 OR 자동 검색
        // -> 사용자의 전화번호가 정상적으로 인증된 것이므로 콜백에 전달된 PhoneAuthCredential 객체를 사용하여
        //    사용자를 로그인 처리할 수 있습니다.
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d("Ourstock", "onVerificationCompleted:$credential")
            verificationInProgress = false

            //updateUI(STATE_VERIFY_SUCCESS, null, credential)
            certification.signInWithPhoneAuthCredential(credential)
        }

        // 잘못된 전화번호 OR 인증 코드가 지정된 경우와 같이, 잘못된 인증 요청에 대한 응답
        override fun onVerificationFailed(e: FirebaseException) {
            Log.w("Ourstock", "onVerificationFailed", e)
            verificationInProgress = false

            if (e is FirebaseAuthInvalidCredentialsException) {
                // 잘못된 요청
                error.postValue(ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), API_CONNECT_ERROR_CODE, ""+e.localizedMessage, activity.getString(R.string.err_invalid_request)+e.localizedMessage, "/register"))
            } else if (e is FirebaseTooManyRequestsException) {
                // 프로젝트에 대한 SMS 전송량이 초과됨
                error.postValue(ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), API_CONNECT_ERROR_CODE, activity.getString(R.string.err_SMS_quota_exceeded), activity.getString(R.string.err_SMS_quota_exceeded), "/register"))
            } else {
                // 기타 오류
                error.postValue(ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), API_CONNECT_ERROR_CODE, activity.getString(R.string.err_unknown), activity.getString(R.string.err_unknown)+e.localizedMessage, "/register"))
            }

            // Show a message and update the UI
            //updateUI(STATE_VERIFY_FAILED, null, null)
        }

        // (선택사항) 이 메소드는 제공된 전화번호로 인증 코드가 SMS를 통해 전송된 후에 호출
        // 대부분의 앱은 사용자에게 SMS 메시지로 받은 인증 코드를 입력하라는 UI를 표시합니다.
        // 동시에 백그라운드에서 자동 인증이 진행될 수도 있습니다.
        // 사용자가 인증 코드를 입력하면 인증 코드와 이 메서드에 전달된 인증 ID를 사용하여 PhoneAuthCredential 객체를 만들고,
        // 이 객체로 사용자를 로그인 처리할 수 있습니다.
        override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d("Ourstock", "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token

            // UI 처리
            //updateUI(STATE_CODE_SENT)
        }

        // (선택사항) 이 메서드는 onVerificationCompleted가 아직 트리거되기 전에
        // verifyPhoneNumber에 지정된 제한시간이 경과된 후에 호출됩니다.
        // 또한 기기에 SIM 카드가 없으면 SMS 자동 검색이 불가능하므로 이 메서드가 즉시 호출됩니다.
        override fun onCodeAutoRetrievalTimeOut(verificationId: String) {
            super.onCodeAutoRetrievalTimeOut(verificationId)

            if(verificationInProgress) {
                // UI 처리
                //updateUI(STATE_VERIFY_FAILED)
            }
        }
    }

    fun success(user: FirebaseUser?) {
        if(user != null) {
            error.postValue(ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), API_CONNECT_ERROR_CODE, "Success", "이름 " + user.displayName, "/register"))
            status.postValue(STATE_SIGNUP_VERIFY_SUCCESS)
            timer.cancel()
        }
        else {
            error.postValue(ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), API_CONNECT_ERROR_CODE, "User is null", "User is null.", "/register"))
            status.postValue(STATE_SIGNUP_VERIFY_FAILED)
            timer.cancel()
        }
    }
    fun failed(errorMessage: String) {
        error.postValue(ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), API_CONNECT_ERROR_CODE, errorMessage, errorMessage, "/register"))
        status.postValue(STATE_SIGNUP_VERIFY_FAILED)
    }

    // CertificationRepository, 전화번호 인증 모듈
    private val certification = CertificationRepositoryImpl(callbacks, ::success, ::failed, activity)

    init {
        // status 값은 STATE_SIGNUP_INPUT_NAME으로 셋팅해두고 시작.
        status.postValue(STATE_SIGNUP_INPUT_NAME)
    }

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

    fun startTimer() {
        timer = TimeoutTimer(120000, 1000)
        timer.start()
        timeEnd.postValue(false)
    }

    fun onLoginButtonClick() {
        when(status.value!!) {
            STATE_SIGNUP_INPUT_NAME -> status.postValue(STATE_SIGNUP_INPUT_NUMBER)
            STATE_SIGNUP_INPUT_NUMBER -> status.postValue(STATE_SIGNUP_INPUT_TELECOM)
            STATE_SIGNUP_INPUT_TELECOM -> status.postValue(STATE_SIGNUP_INPUT_PHONE)
            STATE_SIGNUP_INPUT_PHONE -> {
                status.postValue(STATE_SIGNUP_CODE_SENT)

                if(TextUtils.isEmpty(phone.value))
                    error.postValue(ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), API_CONNECT_ERROR_CODE, "PhoneNumber Empty", "전화번호가 입력되지 않았습니다.", "/register"))
                else
                    certification.startPhoneNumberVerification(phoneNumber(phone.value!!))
            }
            STATE_SIGNUP_CODE_SENT -> {
                if (TextUtils.isEmpty(verification.value)) {
                    error.postValue(ErrorResponse(SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()), API_CONNECT_ERROR_CODE, activity.getString(R.string.err_no_code), activity.getString(R.string.err_no_code), "/register"))
                } else {
                    certification.verifyPhoneNumberWithCode(storedVerificationId, verification.value!!)
                }
            }
        }

        /*if(phone.get() != null && phone.get() != "") {
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
            loading.postValue(false)*/
    }

    inner class TimeoutTimer(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {
        override fun onTick(millisUntilFinished: Long) {
            time.postValue(String.format("%01d", millisUntilFinished/60000) + ":" + String.format("%02d", millisUntilFinished%60000/1000))
        }

        override fun onFinish() {
            time.postValue("0:00")
            timeEnd.postValue(true)
        }
    }
}