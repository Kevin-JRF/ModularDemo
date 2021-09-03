package com.example.modular.bean

data class NewsBean(val reason: String?,
                    val result: ResultBean?,
                    val error_code: Int?,
                    var hasData: Boolean)

data class ResultBean(val stat: String?,val data: Array<DataBean>?,val page: Int?,val pageSize: Int?)

data class DataBean(val uniquekey: String?,
                    val title: String?,
                    val date: String?,
                    val category: String?,
                    val author_name: String?,
                    val url: String?,
                    val thumbnail_pic_s: String?,
                    val thumbnail_pic_s02: String?,
                    val thumbnail_pic_s03: String?)