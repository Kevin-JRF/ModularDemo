package com.example.webview.webviewprocess

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.Toast
import com.example.webview.WebViewCallBack
import org.json.JSONObject

class BaseWebView: WebView{

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr)

    init {
        WebViewProcessDispatcher.initAIDL()
        WebViewDefaultSettings.setSettings(this)
        addJavascriptInterface(JSInterface(context),"app")
    }

    fun setWebViewCallBack(callBack: WebViewCallBack){
        webViewClient = MyWebViewClient(callBack)
        webChromeClient = MyWebChromeClient(callBack)
    }



}