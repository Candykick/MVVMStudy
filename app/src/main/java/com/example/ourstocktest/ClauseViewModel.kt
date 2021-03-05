package com.example.ourstocktest

import android.content.Intent
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.ViewModel
import com.example.ourstocktest.App.Companion.instance
import com.example.ourstocktest.data.repository.ClauseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ClauseViewModel(private val repository: ClauseRepository): ViewModel() {
    /** 약관 체크 시 필요한 값들(Ncs : 필수, Opt : 선택) */
    // (값의 변경이 ViewModel에서만 일어나지 않기 때문에 private로 숨기지 않음.)
    val clauseNcs1 = MutableLiveData<Boolean>(false)
    val clauseNcs2 = MutableLiveData<Boolean>(false)
    val clauseOpt1 = MutableLiveData<Boolean>(false)
    val clauseOpt2 = MutableLiveData<Boolean>(false)

    /** 약관 정보를 웹에서 불러오는 함수. String을 리턴한다. */
    suspend fun showClause(id: Int) : String = repository.showClauseHtml(id)

    /** 약관 페이지에서 '동의' 버튼 클릭 시 이벤트 중 View와 관련없는 부분만. */
    fun agreeClause(id: Int) {
        when(id) {
            1 -> clauseNcs1.postValue(true)
            2 -> clauseNcs2.postValue(true)
            3 -> clauseOpt1.postValue(true)
            4 -> clauseOpt2.postValue(true)
        }

        Log.d("FuckingBug", clauseNcs1.value!!.toString()+" "+clauseNcs2.value!!.toString()+" "+clauseOpt1.value!!.toString()+" "+clauseOpt2.value!!.toString()+" ")
    }

    /** 약관 페이지에서 '거부' 버튼 클릭 시 이벤트 중 View와 관련없는 부분만. */
    fun cancelClause(id: Int) {
        when(id) {
            1 -> clauseNcs1.postValue(false)
            2 -> clauseNcs2.postValue(false)
            3 -> clauseOpt1.postValue(false)
            4 -> clauseOpt2.postValue(false)
        }

        Log.d("FuckingBug", clauseNcs1.value!!.toString()+" "+clauseNcs2.value!!.toString()+" "+clauseOpt1.value!!.toString()+" "+clauseOpt2.value!!.toString()+" ")
    }
}