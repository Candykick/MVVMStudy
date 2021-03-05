package com.example.ourstocktest.extension

import android.webkit.WebView
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("bind:html")
fun html(view: WebView, html: String) {
    view.loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
}

/*@BindingAdapter("phoneText")
fun phoneText(view: EditText, phone: String) {
    if (phone.length >= 11) {
        val ans = phone.substring(0, 3) + "-" + phone.substring(3, 7) + "-" + phone.substring(7)
        view.setText(ans)
        view.setSelection(view.text.length - (view.text.length - view.selectionStart))
    } else if (phone.length >= 6) {
        val ans = phone.substring(0, 3) + "-" + phone.substring(3, 6) + "-" + phone.substring(6)
        view.setText(ans)
        view.setSelection(view.text.length - (view.text.length - view.selectionStart))
    } else if (phone.length >= 3) {
        val ans = phone.substring(0, 3) + "-" + phone.substring(3)
        view.setText(ans)
        view.setSelection(view.text.length - (view.text.length - view.selectionStart))
    }
}*/

/*@BindingAdapter("bind:loadUrl")
fun bindUrlImage(view: ImageView, url: String) {
    Glide.with(view)
            .load(url)
            .into(view)
}*/