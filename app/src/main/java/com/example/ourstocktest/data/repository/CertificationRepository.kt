package com.example.ourstocktest.data.repository

import android.app.Activity
import android.util.Log
import com.example.ourstocktest.R
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

interface CertificationRepository {
    // 전화번호 인증작업 시작 함수
    fun startPhoneNumberVerification(phoneNumber: String)
    // 코드가 맞는지 확인하는 함수
    fun verifyPhoneNumberWithCode(verificationId: String?, code: String)
    // 코드 재전송 함수
    fun resendVerificationCode(phoneNumber: String, token: PhoneAuthProvider.ForceResendingToken?)

    // PhoneAuthCredential로 로그인하는 함수
    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential)
}

class CertificationRepositoryImpl constructor(private val callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks,
                                              private val success: (m: FirebaseUser?) -> Unit,
                                              private val failed: (m: String) -> Unit,
                                              private val activity: Activity) : CertificationRepository {
    var auth = Firebase.auth

    init {
        auth.setLanguageCode("kr")
    }

    override fun startPhoneNumberVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(120L, TimeUnit.SECONDS) // 최대 120초.
                .setActivity(activity)
                .setCallbacks(callbacks)
                .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signInWithPhoneAuthCredential(credential)
    }

    override fun resendVerificationCode(phoneNumber: String, token: PhoneAuthProvider.ForceResendingToken?) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(120L, TimeUnit.SECONDS) // 최대 120초
                .setActivity(activity)
                .setCallbacks(callbacks)
        if(token != null)
            optionsBuilder.setForceResendingToken(token)
        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }

    override fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(activity) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("Ourstock", "signInWithCredential:success")

                        // 유저 정보를 받아서 UI 콜백으로 넘김
                        val user = task.result?.user
                        success(user)
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w("Ourstock", "signInWithCredential:failure", task.exception)
                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            // 인증 코드가 잘못된 경우
                            failed(activity.getString(R.string.err_wrong_code))
                        } else if(task.exception != null) {
                            // 기타 오류
                            failed(activity.getString(R.string.err_unknown)+task.exception!!.message)
                        }
                    }
                }
    }
}


// Google Phone Certification에서만 필요한 (그럴 가능성이 높은) 함수.
// 전화번호에 국가코드를 붙이는 함수
fun phoneNumber(phoneNum: String): String {
    val phoneNumStr = "+82 "+phoneNum.substring(1, phoneNum.length).replace("-","")
    return phoneNumStr
}