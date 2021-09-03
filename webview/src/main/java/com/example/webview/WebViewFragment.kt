package com.example.webview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.annotation.Router
import com.example.loadsir.loadsir.ErrorCallback
import com.example.loadsir.loadsir.LoadingCallback

import com.example.webview.databinding.FragmentWebviewBinding
import com.example.webview.utils.CAN_PULL_DOWN_REFRESH
import com.example.webview.utils.URL
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir

//@Router("webview/WebViewFragment")
class WebViewFragment : Fragment(), WebViewCallBack {

    private var mUrl = ""

    private var mCanPullDownRefresh = true

    private lateinit var mBinding: FragmentWebviewBinding

    private lateinit var mLoadService: LoadService<Any>

    private var mIsError = false

    companion object {
        fun newInstance(url: String,canPullDownRefresh: Boolean): WebViewFragment {
            val fragment = WebViewFragment()
            val bundle = Bundle()
            bundle.putString(URL, url)
            bundle.putBoolean(CAN_PULL_DOWN_REFRESH, canPullDownRefresh)
            fragment.arguments = bundle
            return fragment
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null){
            bundle.getString(URL)?.let { mUrl = it }
            mCanPullDownRefresh = bundle.getBoolean(CAN_PULL_DOWN_REFRESH)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_webview,container,false)
        mLoadService =  LoadSir.getDefault().register(mBinding.refresh) {
            mBinding.web.reload()
        }
        mBinding.web.setWebViewCallBack(this)
        mBinding.refresh.setEnableLoadMore(false)
        mBinding.refresh.setEnableRefresh(mCanPullDownRefresh)
        mBinding.web.loadUrl(mUrl)
        return mLoadService.loadLayout

    }

    override fun pageStart(url: String) {
        mLoadService.showCallback(LoadingCallback::class.java)
    }

    override fun pageFinish(url: String) {
        if (mIsError){
            mLoadService.showCallback(ErrorCallback::class.java)
            mBinding.refresh.setEnableRefresh(false)

        }else{
            mBinding.refresh.setEnableRefresh(mCanPullDownRefresh)
            mLoadService.showSuccess()
        }
        mBinding.refresh.finishRefresh()
        mIsError = false
    }

    override fun onError() {
        mIsError = true
        mBinding.refresh.finishRefresh()
        mLoadService.showCallback(ErrorCallback::class.java)

    }

    override fun onUpdateTitle(title: String) {
        if (activity is WebViewActivity) {
            (activity as WebViewActivity).updateTitle(title)
        }
    }


    fun getWebView(): WebView{
        return mBinding.web
    }

}