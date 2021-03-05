package com.example.ourstocktest

import android.animation.ObjectAnimator
import android.content.DialogInterface
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.ourstocktest.databinding.ActivityMainBinding
import com.example.ourstocktest.dialog.LoadingDialog
import com.example.ourstocktest.dialog.TelecomBottomDialog
import com.example.ourstocktest.model.SingleLiveEvent
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/** 유효성 검사 부분이 View에 포함된 이유 : '모든' Forms의 유효성을 검사하는 가장 깔끔한 방법을 찾다보니 View에 넣는 게 최선이었다.
 *                                  (이 때문에, 유효성 검사에 관한 모든 처리는 View에서만 이루어져야 한다.
 *                                   ViewModel에서는 ViewModel이 들고 있는 MutableLiveData에 대해서만 유효성 검사를 따로 진행한다.
 *                                   이는 혹시라도 View에서 유효성 검사 도중 오류가 발생해서, 잘못된 값이 넘어가는 경우를 대비한 것이다.
 *                                   다만 이런 구조라 할지라도 View - ViewModel의 유효성 검사는 서로 완전히 별개이며, 서로에게 의존성이 있어서는 안 된다.) */

class MainActivity : BaseActivity() {
    // 각 EditText의 유효성 체크 Boolean
    var checkName = false; var checkResident1 = false; var checkResident2 = false; var checkTelecom = false; var checkPhone = false;

    // 알파 애니메이션 객체들
    lateinit var alphaTitleAnimP1: AlphaAnimation
    lateinit var alphaTitleAnimP2: AlphaAnimation
    lateinit var alphaWidgetAnim: AlphaAnimation

    // Loading Dialog 및 MVVM 관련 객체들
    val loadingDialog: LoadingDialog by inject { parametersOf(this@MainActivity) }
    private val binding by binding<ActivityMainBinding>(R.layout.activity_main)
    private val viewModel: ViewModel by viewModel { parametersOf(this@MainActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Restore instance state
        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@MainActivity
            viewModel = this@MainActivity.viewModel
        }

        /** 애니메이션 모음 **/
        // 제목 애니메이션(알파)
        alphaTitleAnimP1 = AlphaAnimation(1.0f, 0.0f)
        alphaTitleAnimP1.duration = 200
        alphaTitleAnimP1.fillAfter = true
        alphaTitleAnimP2 = AlphaAnimation(0.0f, 1.0f)
        alphaTitleAnimP2.duration = 200
        alphaTitleAnimP2.fillAfter = true
        alphaTitleAnimP2.startOffset = 100

        // 위젯 공통 애니메이션(알파)
        alphaWidgetAnim = AlphaAnimation(0.0f, 1.0f)
        alphaWidgetAnim.duration = 400
        alphaWidgetAnim.fillAfter = true

        // 키보드가 화면을 밀어올리도록 구현.
        if (Build.VERSION.SDK_INT < 30)
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)//window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        else {
            window.setDecorFitsSystemWindows(false)
            val controller = window.insetsController
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
        // This constant was deprecated in API level 30.
        // Use WindowInsetsController#hide(int) with Type#statusBars() instead.
        /*else {
            window.setDecorFitsSystemWindows(false)
            val controller = window.insetsController
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }*/

        /** ViewModel에 따른 View의 변화 반영 */
        viewModel.loading.observe(this@MainActivity, Observer {
            if (it) { // loading이 true인 경우
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        })

        viewModel.error.observe(this@MainActivity, Observer {
            Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_LONG).show()
        })

        // ViewModel의 status에 따라서 현재 Activity의 View를 적절히 변경한다.
        // 정확히는, View들에 Animation을 적용하고, 맨 아래 버튼을 상태가 바뀔 때마다 비활성화한다.
        viewModel.status.observe(this@MainActivity, Observer {
            when (it) {
                STATE_SIGNUP_INPUT_NAME -> {
                    // 이름 입력, 초기 상태.
                    vitalizeBtn(false)
                }
                STATE_SIGNUP_INPUT_NUMBER -> {
                    // 주민번호 입력
                    vitalizeBtn(false)

                    binding.tvSignupName.startAnimation(alphaTitleAnimP1)
                    binding.tvSignupName.text = getString(R.string.signup_number_title)
                    binding.tvSignupName.startAnimation(alphaTitleAnimP2)

                    /*val animBtn = ObjectAnimator.ofFloat(binding.btnSignupName, "translationY", (binding.btnSignupName.height.toFloat() + 30) * 2)
                    animBtn.duration = 400*/
                    val animName = ObjectAnimator.ofFloat(binding.etSignupLayoutName, "translationY", binding.etSignupLayoutName.height.toFloat() + 30)
                    animName.duration = 400
                    animName.start()

                    binding.llSignupNumber.visibility = View.VISIBLE
                    binding.llSignupNumber.startAnimation(alphaWidgetAnim)
                }
                STATE_SIGNUP_INPUT_TELECOM -> {
                    // 통신사 선택
                    vitalizeBtn(false)

                    binding.tvSignupName.startAnimation(alphaTitleAnimP1)
                    binding.tvSignupName.text = getString(R.string.signup_telecom_title)
                    binding.tvSignupName.startAnimation(alphaTitleAnimP2)

                    /*val animBtn = ObjectAnimator.ofFloat(binding.btnSignupName, "translationY", (binding.btnSignupName.height.toFloat() + 30) * 3)
                    animBtn.duration = 400*/
                    val animName = ObjectAnimator.ofFloat(binding.etSignupLayoutName, "translationY", (binding.etSignupLayoutName.height.toFloat() + 30) * 2)
                    animName.duration = 400
                    val animNumber = ObjectAnimator.ofFloat(binding.llSignupNumber, "translationY", binding.llSignupNumber.height.toFloat() + 30)
                    animNumber.duration = 400
                    animName.start()
                    animNumber.start()

                    binding.etSignupLayoutTelecom.visibility = View.VISIBLE
                    binding.etSignupLayoutTelecom.startAnimation(alphaWidgetAnim)
                }
                STATE_SIGNUP_INPUT_PHONE -> {
                    // 전화번호 입력
                    vitalizeBtn(false)

                    binding.btnSignupName.text = getString(R.string.signup_btn_get_verification)

                    binding.tvSignupName.startAnimation(alphaTitleAnimP1)
                    binding.tvSignupName.text = getString(R.string.signup_phone_title)
                    binding.tvSignupName.startAnimation(alphaTitleAnimP2)

                    /*val animBtn = ObjectAnimator.ofFloat(binding.btnSignupName, "translationY", (binding.btnSignupName.height.toFloat() + 30) * 4)
                    animBtn.duration = 400*/
                    val animName = ObjectAnimator.ofFloat(binding.etSignupLayoutName, "translationY", (binding.etSignupLayoutName.height.toFloat() + 30) * 3)
                    animName.duration = 400
                    val animNumber = ObjectAnimator.ofFloat(binding.llSignupNumber, "translationY", (binding.llSignupNumber.height.toFloat() + 30) * 2)
                    animNumber.duration = 400
                    val animTelecom = ObjectAnimator.ofFloat(binding.etSignupLayoutTelecom, "translationY", binding.etSignupTelecom.height.toFloat() + 30)
                    animTelecom.duration = 400
                    animName.start()
                    animNumber.start()
                    animTelecom.start()

                    binding.etSignupLayoutPhone.visibility = View.VISIBLE
                    binding.etSignupLayoutPhone.startAnimation(alphaWidgetAnim)
                }

                STATE_SIGNUP_CODE_SENT -> {
                    /** 모든 입력이 완료되고, 인증번호를 전송한 상황. */
                    vitalizeBtn(true)
                    // 휴대폰번호 인증 절차가 시작된 상황이므로 모든 입력 관련 Form을 잠근다.
                    binding.etSignupName.isEnabled = false; binding.etSignupName.isClickable = false; binding.etSignupName.isFocusable = false
                    binding.etSignupNumber1.isEnabled = false; binding.etSignupNumber1.isClickable = false; binding.etSignupNumber1.isFocusable = false
                    binding.etSignupNumber2.isEnabled = false; binding.etSignupNumber2.isClickable = false; binding.etSignupNumber2.isFocusable = false
                    binding.etSignupTelecom.isEnabled = false; binding.etSignupTelecom.isClickable = false; binding.etSignupTelecom.isFocusable = false
                    binding.etSignupPhone.isEnabled = false; binding.etSignupPhone.isClickable = false; binding.etSignupPhone.isFocusable = false

                    // 인증번호 폼을 띄운다.
                    binding.btnSignupName.text = getString(R.string.signup_btn_finish_verification)

                    binding.tvSignupName.startAnimation(alphaTitleAnimP1)
                    binding.tvSignupName.text = getString(R.string.signup_verification_title)
                    binding.tvSignupName.startAnimation(alphaTitleAnimP2)

                    /*val animBtn = ObjectAnimator.ofFloat(binding.btnSignupName, "translationY", (binding.btnSignupName.height.toFloat() + 30) * 5)
                    animBtn.duration = 400*/
                    val animName = ObjectAnimator.ofFloat(binding.etSignupLayoutName, "translationY", (binding.etSignupLayoutName.height.toFloat() + 30) * 4)
                    animName.duration = 400
                    val animNumber = ObjectAnimator.ofFloat(binding.llSignupNumber, "translationY", (binding.llSignupNumber.height.toFloat() + 30) * 3)
                    animNumber.duration = 400
                    val animTelecom = ObjectAnimator.ofFloat(binding.etSignupLayoutTelecom, "translationY", (binding.etSignupTelecom.height.toFloat() + 30) * 2)
                    animTelecom.duration = 400
                    val animPhone = ObjectAnimator.ofFloat(binding.etSignupLayoutPhone, "translationY", binding.etSignupPhone.height.toFloat() + 30)
                    animPhone.duration = 400
                    animName.start()
                    animNumber.start()
                    animTelecom.start()
                    animPhone.start()

                    binding.llSignupLayoutVerify.visibility = View.VISIBLE
                    binding.llSignupLayoutVerify.startAnimation(alphaWidgetAnim)
                    binding.lineSignupLayoutVerify.visibility = View.VISIBLE
                    binding.lineSignupLayoutVerify.startAnimation(alphaWidgetAnim)

                    // 타이머를 시작한다.
                    // 원래는 ViewModel에서 상태 변환 시 시작시키려고 했으나, 모든 View가 Animation된 이후에 Timer가 시작되어야 하기 때문에, Timer 시작 함수를 따로 만들었다.
                    viewModel.startTimer()
                }
            }
        })

        // 이름의 유효성 검사.
        viewModel.username.observe(this@MainActivity, Observer { name ->
            checkName = name.length in NAME_MIN_LENGTH..NAME_MAX_LENGTH
            checkButtonValidation()
        })

        // 주민번호의 유효성 검사 + 앞/뒷자리 EditText의 focus 이동 처리
        viewModel.residentNumberFront.observe(this@MainActivity, Observer { front ->
            checkResident1 = (front.length == RESIDENT_NUM_FRONT_LENGTH)
            checkButtonValidation()

            if(checkResident1)
                binding.etSignupNumber2.setSelection(0)
        })
        viewModel.residentNumberBack.observe(this@MainActivity, Observer { back ->
            checkResident2 = (back.length == RESIDENT_NUM_BACK_LENGTH)
            checkButtonValidation()

            if(back.isNullOrEmpty())
                binding.etSignupNumber1.setSelection(binding.etSignupNumber1.text!!.length)
        })

        // 통신사 유효성 검사
        viewModel.telecom.observe(this@MainActivity, Observer { telecom ->
            checkTelecom = telecom.isNotBlank()
            checkButtonValidation()
        })

        // 전화번호 유효성 검사
        viewModel.phone.observe(this@MainActivity, Observer { phone ->
            checkPhone = phone.length in PHONE_MIN_LENGTH..PHONE_MAX_LENGTH
            checkButtonValidation()
        })

        // 인증번호 전송 후, 타이머 종료 검사
        viewModel.timeEnd.observe(this@MainActivity, Observer { isEnd ->
            if(isEnd)
                Toast.makeText(this@MainActivity, getString(R.string.err_login_timeout), Toast.LENGTH_LONG).show()
        })


        /** 기타 View에 대해 직접 설정 */
        // 통신사 EditText 클릭 시 통신사를 선택하는 Bottom Dialog를 띄운다.
        binding.etSignupTelecom.setOnClickListener {
            val bottomSheet = TelecomBottomDialog(this, resources.getStringArray(R.array.menu_telecom))
            val dialogListener = object : TelecomBottomDialog.BottomDialogListener {
                override fun onClicked(data: String) {
                    binding.etSignupTelecom.setText(data)
                }
            }
            bottomSheet.dialogListener = dialogListener
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }

        binding.etSignupPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher())
    }

    override fun onStop() {
        super.onStop()
        loadingDialog.dismiss()
    }

    /** 뒤로가기 최종 동작
     *  1. API 호출 중 : LoadingDialog를 내리고, API Call을 취소한다.
     *  2. 기타 : 처음으로 되돌아갈 것인지를 묻는 Dialog를 띄운다.
     */
    override fun onBackPressed() {
        // 현재 : LoadingDialog가 떠 있을 때는 뒤로가기 버튼이 동작하지 않음. ->  LoadingDialog가 떠 있을 때는 API 호출을 취소하는 방향으로.
        if (!loadingDialog.isShowing) {
            AlertDialog.Builder(this@MainActivity)
                    .setTitle("처음으로 되돌아가시겠습니까?")
                    .setMessage("현재까지 기록한 모든 정보가 삭제됩니다.")
                    .setPositiveButton("뒤로가기", DialogInterface.OnClickListener { dialog, which -> super.onBackPressed() })
                    .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                    .show()
        }
    }

    // 현 Status에서 입력해야 하는 모든 Form들에 정상적인 값이 들어가 있는지를 확인해서, Button의 활성화 여부를 결정하는 함수.
    // 각 입력 Form에 해당하는 LiveData의 Observe 함수에서 호출된다.
    fun checkButtonValidation() {
        when(viewModel.status.value) {
            STATE_SIGNUP_INPUT_NAME -> {
                // 이름의 유효성만 검사
                if(checkName)
                    vitalizeBtn(true)
                else
                    vitalizeBtn(false)
            }
            STATE_SIGNUP_INPUT_NUMBER -> {
                // 이름, 주민번호 앞자리, 주민번호 뒷자리의 유효성만 검사
                if(checkName && checkResident1 && checkResident2)
                    vitalizeBtn(true)
                else
                    vitalizeBtn(false)
            }
            STATE_SIGNUP_INPUT_TELECOM -> {
                // 이름, 주민번호 앞자리, 주민번호 뒷자리, 통신사의 유효성 검사
                if(checkName && checkResident1 && checkResident2 && checkTelecom)
                    vitalizeBtn(true)
                else
                    vitalizeBtn(false)
            }
            STATE_SIGNUP_INPUT_PHONE -> {
                // 이름, 주민번호 앞자리, 주민번호 뒷자리, 통신사, 전화번호의 유효성 검사
                if(checkName && checkResident1 && checkResident2 && checkTelecom && checkPhone)
                    vitalizeBtn(true)
                else
                    vitalizeBtn(false)
            }
        }
    }

    // 위의 checkButtonValidation 보조 함수. btnSignupName 활성화/비활성화.
    // 참고로 status 변경 시 아래 버튼을 비활성화하는 데에도 쓰인다.
    private fun vitalizeBtn(bool: Boolean) {
        binding.btnSignupName.isEnabled = bool
        binding.btnSignupName.isClickable = bool
        binding.btnSignupName.isFocusable = bool
    }

    companion object {
        // 이름 : 2자~17자
        const val NAME_MIN_LENGTH = 2
        const val NAME_MAX_LENGTH = 17
        // 주민등록번호 앞자리/뒷자리 수
        const val RESIDENT_NUM_FRONT_LENGTH = 6
        const val RESIDENT_NUM_BACK_LENGTH = 1
        // 전화번호 : - 포함, 11~14자
        const val PHONE_MIN_LENGTH = 11
        const val PHONE_MAX_LENGTH = 14
    }
}