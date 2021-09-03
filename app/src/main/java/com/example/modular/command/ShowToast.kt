package com.example.modular.command


import android.util.Log
import com.example.webview.command.Command
import com.google.auto.service.AutoService


@AutoService(Command::class)
class ShowToast: Command {
    override fun name(): String = "showToast"

    override fun execute(params: String) {
        Log.d("showToast", "execute: =================================================")
//        Handler(Looper.getMainLooper()).post{
//            Toast.makeText(BaseApplication.sApplication,"成功",Toast.LENGTH_LONG).show()
//
//        }
    }
}