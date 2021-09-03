package com.example.modular.request

import android.text.TextUtils
import com.example.common.net.api.RetrofitClient
import com.example.common.net.request.BaseRequest
import com.example.common.utils.getSpValue
import com.example.common.utils.putSpValue
import com.example.modular.apiService.MainService
import com.example.modular.bean.NewsBean
import com.google.gson.Gson

class MainRequest: BaseRequest() {

    suspend fun getNews(newsKind: String,isSaveLocal:Boolean = false): NewsBean? {
//        return cacheNetCall(remote = {
//            RetrofitClient.create(MainService::class.java).getNews(newsKind)
//        },local = {
//            val json = getSpValue(key = NewsBean::class.java.name, defaultVal = "{}")
//            //获取缓存数据
//            Gson().fromJson(
//                json,
//                NewsBean::class.java
//            )
//        },save = {
//            val spValue = getSpValue(key = NewsBean::class.java.name, defaultVal = "")
//            if (isSaveLocal || TextUtils.isEmpty(spValue)) {
//                //如果第一次网络请求成功，本地缓存为空进行缓存
//                putSpValue(key = NewsBean::class.java.name, value = Gson().toJson(it))
//            }
//        })
        return call(remote = {
            RetrofitClient.create(MainService::class.java).getNews(newsKind)
        },save = {
            val spValue = getSpValue(key = NewsBean::class.java.name, defaultVal = "")
            if (isSaveLocal || TextUtils.isEmpty(spValue)) {
                //如果第一次网络请求成功，本地缓存为空进行缓存
                putSpValue(key = NewsBean::class.java.name, value = Gson().toJson(it))
            }
        })
    }





}