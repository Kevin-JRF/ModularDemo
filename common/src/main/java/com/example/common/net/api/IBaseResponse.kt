package com.example.common.net.api

interface IBaseResponse<T> {
    fun code(): String
    fun msg(): String
    fun data(): T
}