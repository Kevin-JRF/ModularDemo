package com.example.common.router

import android.app.Activity

interface IRouter {

    fun load(routerMap: HashMap<String,Class<out Activity>>): Unit

}