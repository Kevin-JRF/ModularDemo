package com.example.common.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.net.api.ApiException
import com.example.common.net.config.NetConfig
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


open class BaseViewModel : ViewModel(), LifecycleObserver {

    val loadingStatus = MutableLiveData<Boolean>()

    /**
     * 所有网络请求都在 viewModelScope 域中启动，当页面销毁时会自动
     * 调用ViewModel的  #onCleared 方法取消所有协程
     */
    private fun launch(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(Dispatchers.Main) { block() }

    /**
     * 用流的方式进行网络请求
     */
    fun <T> launchFlow(block: suspend () -> T): Flow<T> {
        return flow {
            emit(block())
        }
    }


    fun runUIThread(callback: (()->Unit)?){
        viewModelScope.launch(Dispatchers.Main) { callback?.invoke() }
    }




    /**
     *  不过滤请求结果
     * @param block 请求体
     * @param error 失败回调
     * @param cancel 取消回调
     * @param complete  完成回调（无论成功失败都会调用）
     * @param showLoading 是否显示加载框
     */
    /* fun request(
         block: suspend CoroutineScope.() -> Unit,
         cancel: suspend CoroutineScope.() -> Unit = {},
         error: suspend CoroutineScope.(ApiException) -> Unit = {
             //   defUI.toastEvent.postValue("${it.code}:${it.errMsg}")
         },
         complete: suspend CoroutineScope.() -> Unit = {},
         showLoading: Boolean = true
     ) {
         if (showLoading)
             loadingStatus.postValue(true)
         launch {
             handleException({
                 block()
             },
                 { cancel() },
                 { error(it) },
                 {
                     loadingStatus.postValue(false)
                     complete()
                 }
             )
         }
     }*/

    /**
     * 过滤请求结果，其他全抛异常
     * @param block 请求体
     * @param success 成功回调
     * @param error 失败回调
     * @param cancel 取消回调
     * @param complete  完成回调（无论成功失败都会调用）
     * @param showLoading 是否显示加载框
     */
    fun <T> request(
        block: suspend CoroutineScope.() -> T,
        success: (T) -> Unit,
        cancel: suspend CoroutineScope.() -> Unit = {},
        error: (ApiException) -> Unit = {

        },
        complete: () -> Unit = {},
        showLoading: Boolean = true
    ) {
        if (showLoading)
            loadingStatus.postValue(true)
        launch {
            handleException(
                {
                    withContext(Dispatchers.IO) {
                        block()
                    }.also {
                        success(it)
                    } ?: kotlin.run {
                        //此处处理返回空值
                    }
                },
                {
                    cancel()
                },
                {
                    error(it)
                },
                {
                    loadingStatus.postValue(false)
                    complete()
                }
            )
        }
    }

    /**
     * 取消协程
     * @param job 协程job
     */
    protected fun cancelJob(job: Job?) {
        if (job != null && job.isActive && !job.isCompleted && !job.isCancelled) {
            job.cancel()
        }
    }

    /**
     * 异常统一处理
     */
    private suspend fun handleException(
        block: suspend CoroutineScope.() -> Unit,
        cancel: suspend CoroutineScope.() -> Unit,
        error: suspend CoroutineScope.(ApiException) -> Unit,
        complete: suspend CoroutineScope.() -> Unit
    ) {
        coroutineScope {
            try {
                block()
            } catch (e: Throwable) {
                e.printStackTrace()
                if (e is CancellationException) {
                    cancel()
                } else {
                    error(NetConfig.getConfig().globalExceptionHandle(e))
                }
            } finally {
                complete()
            }
        }
    }
}