package com.example.modular.viewModel

import androidx.lifecycle.MutableLiveData
import com.example.common.base.BaseViewModel
import com.example.modular.request.MainRequest
import com.example.modular.bean.NewsBean

class MainViewModel: BaseViewModel() {

    private val mMainRequest = MainRequest()

    val newsData = MutableLiveData<NewsBean>()

    private val TAG = "MainViewModel"

    fun getNewsData(newKind: String,isSaveLocal:Boolean = false){
        request(block = {
            mMainRequest.getNews(newKind,isSaveLocal)
        },success = {
            newsData.postValue(it)
        },error = {

        },complete = {

        })
    }


}