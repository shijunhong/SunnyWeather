package com.sunnyweather.android.logic.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

object ServiceCreator {
    private const val BASE_URL = "https://api.caiyunapp.com/"

    val client = OkHttpClient.Builder()
        .addInterceptor(LogInterceptor())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    inline fun <reified T> create() : T = create(T::class.java)

    class LogInterceptor : Interceptor {

        val tag = "Retrofit"
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")

        override fun intercept(chain: Interceptor.Chain): Response {

            var request = chain.request()

            Log.i(tag, format.format(Date()) + " Requeste " + "\nmethod:" + request.method() + "\nurl:" + request.url() + "\nbody:" + request.body())

            var response = chain.proceed(request)

            //response.peekBody不会关闭流
            Log.i(tag, format.format(Date()) + " Response " + "\nsuccessful:" + response.isSuccessful + "\nbody:" + response.peekBody(1024)?.string())

            return response
        }

    }
}