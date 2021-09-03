package com.example.webview.webviewprocess

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.example.common.base.BaseApplication
import com.example.webview.IWebToMainAidlInterface
import com.example.webview.mainprocess.MainProcessCommandService

object WebViewProcessDispatcher : ServiceConnection{

    private var iWebToMainAidlInterface: IWebToMainAidlInterface? = null

    fun initAIDL(){
        val intent = Intent(BaseApplication.sApplication,MainProcessCommandService::class.java)
        BaseApplication.sApplication.bindService(intent,this,Context.BIND_AUTO_CREATE)
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        iWebToMainAidlInterface = IWebToMainAidlInterface.Stub.asInterface(service)
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        iWebToMainAidlInterface = null
        initAIDL()
    }


    override fun onBindingDied(name: ComponentName?) {
        iWebToMainAidlInterface = null
        initAIDL()
    }

    fun executeCommand(commandName: String,params: String){
        iWebToMainAidlInterface?.handleWebCommand(commandName,params)
    }

}