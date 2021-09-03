package com.example.webview.webviewprocess

import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.example.webview.WebViewCallBack

class MyWebChromeClient(private val callBack: WebViewCallBack): WebChromeClient() {

    override fun onReceivedTitle(view: WebView?, title: String?) {
        title?.let {
            callBack.onUpdateTitle(it)
        }
    }

    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        Log.d("webLog", "onConsoleMessage: ${consoleMessage?.message()}")
        return super.onConsoleMessage(consoleMessage)
    }



}