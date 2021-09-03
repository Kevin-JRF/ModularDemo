package com.example.webview.webviewprocess

import android.content.Context
import android.webkit.JavascriptInterface
import org.json.JSONObject

class JSInterface(private val context: Context) {

    @JavascriptInterface
    fun takeNativeAction(params: String){
        val jsonObject = JSONObject(params)
        val name = jsonObject.getString("name")
        val param = jsonObject.getString("param")
        WebViewProcessDispatcher.executeCommand(name,param)

    }
}