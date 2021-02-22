package com.example.ourstocktest

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.ourstocktest.data.repository.UserRepository
import com.example.ourstocktest.di.loadingModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import java.lang.AssertionError

class ViewModel(private val repository: UserRepository): ViewModel() {
    val phone = ObservableField<String>()
    val loading = MutableLiveData<Boolean>()
    val response = MutableLiveData<String>("로그인 안 함")
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
            loadingModule

            loading.postValue(true)
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    response.postValue(repository.login(phone.get()!!))
                    loading.postValue(false)
                } catch (e: Exception) {
                    if(e.localizedMessage != null)
                        response.postValue("Error! : " + e.localizedMessage)
                    else
                        response.postValue("Error! : Undefined Error.")
                    loading.postValue(false)
                }
            }
        } else
            loading.postValue(false)
    }
}