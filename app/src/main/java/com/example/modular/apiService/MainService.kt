package com.example.modular.apiService

import com.example.modular.bean.NewsBean
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap


interface MainService {

    @GET("toutiao/index")
    suspend fun getNews(@Query("type") type: String,@Query("key") key: String = "9eacc1a90ba2f55c116bfd7a16e26bc3"): Response<NewsBean?>

}