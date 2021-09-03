package com.example.common.utils

import android.content.Context
import android.os.Build
import dalvik.system.DexClassLoader
import dalvik.system.DexFile
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

object ClassUtil {


    fun getFileNameByPackage(context: Context,packageName: String): Set<String>{
        val classNames = HashSet<String>()
        val paths = getSourcePaths(context)
        for (path in paths) {
            var dexFile: DexFile? = null
            try {
                dexFile = DexFile(path)
                val dexEntries = dexFile?.entries()
                while (dexEntries?.hasMoreElements() == true){
                    val className = dexEntries.nextElement()
                    if (className.startsWith(packageName)){
                        classNames.add(className)
                    }
                }
            }catch (e: IOException){
                e.printStackTrace()
            }finally {
                if (null != dexFile){
                    try {
                        dexFile.close()
                    }catch (e: IOException){
                        e.printStackTrace()
                    }
                }
            }
        }
        return classNames
    }



    private fun getSourcePaths(context: Context): List<String>{
        val applicationInfo = context.packageManager.getApplicationInfo(context.packageName,0)
        val sourcePaths = ArrayList<String>()
        sourcePaths.add(applicationInfo.sourceDir)
        if (null != applicationInfo.splitSourceDirs){
            sourcePaths.addAll(applicationInfo.splitSourceDirs)
        }
        return sourcePaths
    }
}