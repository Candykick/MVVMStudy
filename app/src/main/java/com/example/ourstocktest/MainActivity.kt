package com.example.ourstocktest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ourstocktest.data.repository.UserRepository
import com.example.ourstocktest.data.repository.UserRepositoryImpl
import com.example.ourstocktest.databinding.ActivityMainBinding
import com.example.ourstocktest.dialog.LoadingDialog
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

/** 뒤로가기 누르면 API 호출 중지 (뒤로가기를 막는 방법도 있긴 한데, 굳이?) */

class MainActivity : BaseActivity() {
    private val binding by binding<ActivityMainBinding>(R.layout.activity_main)
    private val viewModel: ViewModel by viewModel{ parametersOf() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner = this@MainActivity
            viewModel = this@MainActivity.viewModel
        }

        if(viewModel.loading.value != null && viewModel.loading.value!!) {
            loadingDialog.show()
        } else {
            loadingDialog.dismiss()
        }
    }
}