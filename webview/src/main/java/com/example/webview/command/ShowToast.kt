package com.example.webview.command


import android.util.Log
import com.google.auto.service.AutoService


@AutoService(Command::class)
class ShowToast: Command {
    override fun name(): String = "showToast"

    override fun execute(params: String) {
        Log.d("showToast", "execute: ======================$params===========================")
    }
}