package com.example.common.net.request

import com.example.common.net.api.ApiException
import com.example.common.net.error.ERROR
import retrofit2.Response


abstract class BaseRequest {


    /**
     * @param remoto 网络数据
     * @param local 本地数据
     * @param save 当网络请求成功后，保存数据等操作
     * @param isUseCache 是否使用缓存
     */
    suspend fun <T> cacheNetCall(
        remote: suspend () -> Response<T>,
        local: (() -> T?)? = null,
        save: ((T) -> Unit)? = null,
        isUseCache: (T,Exception?) -> Boolean = { _, exception -> exception!=null }
    ): T? {
        val localData = local?.invoke()
        var remoteData: T? = null
        var exception: Exception? = null
        try {
            remoteData = call(remote, save)
        } catch (e: Exception) {
            e.printStackTrace()
            exception = e
        }
        return if (localData != null && isUseCache(localData,exception)) {
            localData
        } else {
            remoteData
        }
    }

    suspend fun <T> call(
        remote: suspend () -> Response<T>,
        save: ((T) -> Unit)? = null,
    ): T = remote().let {
        val code = it.code()
        if (code in 200..299) {
            it.body()?.also { t ->
                save?.invoke(t)
            } ?: kotlin.run {
                throw ApiException(ERROR.UNKNOWN)
            }
        } else {
            throw ApiException(code.toString(),it.errorBody().toString())
        }

    }


    fun onCleared() {

    }
}