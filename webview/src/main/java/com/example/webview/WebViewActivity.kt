package com.example.webview

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.annotation.Router
import com.example.webview.databinding.ActivityWebviewBinding
import com.example.webview.utils.IS_SHOW_ACTION_BAR
import com.example.webview.utils.TITLE
import com.example.webview.utils.URL

@Router("webview/WebViewActivity")
class WebViewActivity : AppCompatActivity(){

    private val mBinding : ActivityWebviewBinding by lazy{
        DataBindingUtil.setContentView(this, R.layout.activity_webview)
    }

    private var fragment: WebViewFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }


    private fun initView(){
        mBinding.titleBar.visibility = if (intent.getBooleanExtra(IS_SHOW_ACTION_BAR,true)) View.VISIBLE else View.GONE
        mBinding.tvTitle.text = intent.getStringExtra(TITLE)
        mBinding.ivBack.setOnClickListener {
            finish()
        }
        intent.extras?.getSerializable(URL)?.let {
            val fragmentManage = supportFragmentManager
            val transaction = fragmentManage.beginTransaction()
            fragment = WebViewFragment.newInstance(it.toString(), true)
            transaction.replace(R.id.web_fragment, fragment!!).commit()
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        fragment?.apply {
            getWebView().run {
                if (canGoBack()) {
                    goBack()
                }else{
                    finish()
                }
            }
            return true
        }
        return false
    }


    fun updateTitle(title: String){
        mBinding.tvTitle.text = title
    }
}