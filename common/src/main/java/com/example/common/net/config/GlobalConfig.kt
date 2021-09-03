package com.example.common.net.config

import com.example.common.net.error.ExceptionHandle


interface GlobalConfig {
    fun globalExceptionHandle(e: Throwable) = ExceptionHandle.handleException(e)
}