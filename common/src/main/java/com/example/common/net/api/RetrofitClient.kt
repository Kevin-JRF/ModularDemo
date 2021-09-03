package com.example.common.net.api

import android.util.Log
import com.example.common.base.BaseApplication
import com.example.common.net.convert.NullConverterFactory
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

import java.util.concurrent.TimeUnit


object RetrofitClient {

    lateinit var BASE_URL: String
    lateinit var mInterceptors: List<Interceptor>

    fun setup(baseUrl: String, interceptors: List<Interceptor>) {
        BASE_URL = baseUrl
        mInterceptors = interceptors
    }

    private var newRetrofit: Retrofit? = null
    fun update(baseUrl: String, interceptors: List<Interceptor>) {
        BASE_URL = baseUrl
        mInterceptors = interceptors
        newRetrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(NullConverterFactory.create())//解析空body
            .addConverterFactory(ScalarsConverterFactory.create())//转换为String对象
            .addConverterFactory(GsonConverterFactory.create())//转换为Gson对象
            .build()
    }


    /**Cookie*/
    private val cookiePersistor by lazy {
        SharedPrefsCookiePersistor(BaseApplication.sApplication)
    }
    private val cookieJar by lazy { PersistentCookieJar(SetCookieCache(), cookiePersistor) }

    /**log**/
    private val logger = HttpLoggingInterceptor.Logger {
        Log.i(this::class.simpleName, it)
    }
    private val logInterceptor = HttpLoggingInterceptor(logger).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**OkhttpClient*/
    private val okHttpClient by lazy {
        val build = OkHttpClient.Builder()
            .callTimeout(30, TimeUnit.SECONDS)
//            .cookieJar(cookieJar)
            .addNetworkInterceptor(logInterceptor)
        mInterceptors.forEach { inter ->
            build.addInterceptor(inter)
        }

        build.build()
    }

    /**Retrofit*/
    private val retrofit by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(NullConverterFactory.create())//解析空body
            .addConverterFactory(ScalarsConverterFactory.create())//转换为String对象
            .addConverterFactory(GsonConverterFactory.create())//转换为Gson对象
            .build()

    }

    fun <T> create(service: Class<T>?): T =
        newRetrofit?.create(service!!) ?: retrofit.create(service!!)
        ?: throw RuntimeException("Api service is null!")

    /**清除Cookie*/
    fun clearCookie() = cookieJar.clear()

    /**是否有Cookie*/
    fun hasCookie() = cookiePersistor.loadAll().isNotEmpty()
}