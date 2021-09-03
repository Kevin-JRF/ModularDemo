package com.example.common.net.convert

import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class NullConverterFactory private constructor() : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val delegate: Converter<ResponseBody, *> =
            retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
        return Converter { body ->
            if (body == null || body.contentLength() == 0L) {
                null
            } else delegate.convert(body)
        }
    }

    companion object {
        fun create(): NullConverterFactory {
            return NullConverterFactory()
        }
    }
}