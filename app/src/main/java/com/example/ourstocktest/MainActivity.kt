package com.example.ourstocktest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
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
    val loadingDialog: LoadingDialog by inject { parametersOf(this@MainActivity) }
    private val binding by binding<ActivityMainBinding>(R.layout.activity_main)
    private val viewModel: ViewModel by viewModel{ parametersOf() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            lifecycleOwner = this@MainActivity
            viewModel = this@MainActivity.viewModel
        }

        viewModel.loading.observe(this@MainActivity, Observer {
            if(it) { // loading이 true인 경우
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        })

        viewModel.error.observe(this@MainActivity, Observer {
            Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_LONG).show()
        })
    }

    override fun onStop() {
        super.onStop()
        loadingDialog.dismiss()
    }
}