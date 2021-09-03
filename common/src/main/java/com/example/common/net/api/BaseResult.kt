package com.example.common.net.api


data class BaseResult<T>(
    val code: String,
    val msg: String,
    val data: T
) : IBaseResponse<T> {
    override fun code() = code
    override fun msg() = msg
    override fun data() = data
}