package com.example.common.base

import android.app.Application
import com.example.common.router.RouterManager
import com.example.loadsir.loadsir.*
import com.kingja.loadsir.core.LoadSir

class BaseApplication: Application() {

    init {
        sApplication = this
    }

    companion object{
        lateinit var sApplication: Application
    }

    override fun onCreate() {
        super.onCreate()
        RouterManager.init(this)
        LoadSir.beginBuilder()
            .addCallback(ErrorCallback()) //添加各种状态页
            .addCallback(EmptyCallback())
            .addCallback(LoadingCallback())
            .addCallback(TimeoutCallback())
            .addCallback(CustomCallback())
            .setDefaultCallback(LoadingCallback::class.java) //设置默认状态页
            .commit()
    }


}