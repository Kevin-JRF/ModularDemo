package com.example.webview

interface WebViewCallBack {
    fun pageStart(url: String)

    fun pageFinish(url: String)

    fun onError()

    fun onUpdateTitle(title: String)
}