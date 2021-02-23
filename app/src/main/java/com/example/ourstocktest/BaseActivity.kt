package com.example.ourstocktest

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.ourstocktest.dialog.LoadingDialog
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

open class BaseActivity : AppCompatActivity() {
    protected inline fun <reified T : ViewDataBinding> binding(@LayoutRes resId: Int): Lazy<T> =
        lazy { DataBindingUtil.setContentView<T>(this, resId) }
}