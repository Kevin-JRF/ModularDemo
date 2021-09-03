package com.example.webview.webviewprocess

import android.graphics.Bitmap
import android.webkit.*
import com.example.webview.WebViewCallBack

class MyWebViewClient(private val callBack: WebViewCallBack): WebViewClient() {


    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        url?.let {
            callBack.pageStart(it)
        }
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        url?.let {
            callBack.pageFinish(it)
        }
    }


    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        callBack.onError()
    }


    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        super.onReceivedHttpError(view, request, errorResponse)
    }

}