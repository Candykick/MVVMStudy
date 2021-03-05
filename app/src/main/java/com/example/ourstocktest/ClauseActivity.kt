package com.example.ourstocktest

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.ourstocktest.databinding.ActivityClauseBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import org.koin.androidx.viewmodel.ext.android.viewModel

const val CLAUSE_AGREED = 0
const val CLAUSE_CANCELED = -1

class ClauseActivity : BaseActivity() {

    // Loading Dialog 및 MVVM 관련 객체들
    private val binding by binding<ActivityClauseBinding>(R.layout.activity_clause)
    private val viewModel: ClauseViewModel by viewModel { parametersOf(this@ClauseActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Restore instance state
        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState)

        binding.apply {
            activity = this@ClauseActivity
            lifecycleOwner = this@ClauseActivity
            viewModel = this@ClauseActivity.viewModel
        }
    }

    /** 각 약관 버튼 클릭 이벤트 */
    val requestActivity: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->
        if(activityResult.data != null) {
            when (activityResult.resultCode) {
                CLAUSE_AGREED -> viewModel.agreeClause(activityResult.data!!.getIntExtra("id", 0))
                CLAUSE_CANCELED -> viewModel.cancelClause(activityResult.data!!.getIntExtra("id", 0))
            }
        }
    }

    fun showClause(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val contents = viewModel.showClause(id)

            val intent = Intent(this@ClauseActivity, ClauseWebviewActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("contents", contents)
            requestActivity.launch(intent)
        }
    }

    /** '시작하기' 버튼 클릭 이벤트 */
    fun gotoNext() {
        // 동의 필수 항목이 모두 체크되었는지 확인 후 Login
        if(viewModel.clauseNcs1.value!! && viewModel.clauseNcs2.value!!) {
            startActivity(Intent(this@ClauseActivity, MainActivity::class.java))
        } else {
            Toast.makeText(this@ClauseActivity, getString(R.string.clause_no_permission), Toast.LENGTH_LONG).show()
        }
    }
}
