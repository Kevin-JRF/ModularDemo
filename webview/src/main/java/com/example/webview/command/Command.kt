package com.example.webview.command

interface Command {
    fun name(): String
    fun execute(params: String)
}