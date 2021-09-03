package com.example.common.router

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.common.utils.ClassUtil
import java.util.*
import kotlin.collections.HashMap

object RouterManager {


    private val mRouterMap = HashMap<String,Class<out Activity>>()



    fun init(application: Application){
        kotlin.runCatching {

//            ServiceLoader.load(IRouter::class.java).iterator().forEach {
//                it.load(mRouterMap)
//            }

            val classNames =
                ClassUtil.getFileNameByPackage(application, "com.example.router.generate")
            for (className in classNames) {
                val clazz = Class.forName(className)
                if (IRouter::class.java.isAssignableFrom(clazz)){
                    val loader = clazz.newInstance() as IRouter
                    loader.load(mRouterMap)
                }
            }
        }
    }



    fun startActivity(path: String,context: Context,bundle:Bundle? = null){
        if (mRouterMap.containsKey(path)){
            val intent = Intent(context, mRouterMap[path])
            bundle?.let { intent.putExtras(it) }
            context.startActivity(intent)
        }else{
            throw RuntimeException("There is no such activity in routerMap")
        }

    }

//    fun getFragment(path: String): Fragment{
//        if (mRouterMap.containsKey(path)){
//            return mRouterMap[path]
//        }
//    }


}