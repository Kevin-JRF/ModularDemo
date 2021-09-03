package com.example.webview.mainprocess

import com.example.webview.IWebToMainAidlInterface
import com.example.webview.command.Command
import java.util.*
import kotlin.collections.HashMap


object MainProcessCommandManager : IWebToMainAidlInterface.Stub() {
    private val mCommandMap = HashMap<String,Command>()

    init {
        val serviceLoader = ServiceLoader.load(Command::class.java)
        for (command in serviceLoader) {
            if (!mCommandMap.containsKey(command.name()))
                mCommandMap[command.name()] = command
        }


    }


    override fun handleWebCommand(commandNmae: String?, jsonParams: String?) {
        if (jsonParams != null) {
            mCommandMap[commandNmae]?.execute(jsonParams)
        }
    }
}