package com.example.ourstocktest

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.ourstocktest.databinding.ActivityClauseWebviewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class ClauseWebviewActivity : BaseActivity() {

    // Loading Dialog 및 MVVM 관련 객체들
    private val binding by binding<ActivityClauseWebviewBinding>(R.layout.activity_clause_webview)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Restore instance state
        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState)

        binding.apply {
            activity = this@ClauseWebviewActivity
            lifecycleOwner = this@ClauseWebviewActivity
        }

        // 약관 보여주기
        val contents = intent.getStringExtra("contents")
        if(TextUtils.isEmpty(contents)) {

            Toast.makeText(this@ClauseWebviewActivity, "약관을 불러오는 도중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            finish()
        } else
            binding.wvClause.loadDataWithBaseURL(null, contents!!, "text/html", "utf-8", null)
    }

    /** Event 관리 */
    fun agreeClause() {
        val cIntent = Intent()
        cIntent.putExtra("id", intent.getIntExtra("id", 0))
        setResult(CLAUSE_AGREED, cIntent)
        finish()
    }

    fun cancelClause() {
        val cIntent = Intent()
        cIntent.putExtra("id", intent.getIntExtra("id", 0))
        setResult(CLAUSE_CANCELED, cIntent)
        finish()
    }

    override fun onBackPressed() {
        val cIntent = Intent()
        cIntent.putExtra("id", intent.getIntExtra("id", 0))
        setResult(CLAUSE_CANCELED, cIntent)
        finish()
    }
}