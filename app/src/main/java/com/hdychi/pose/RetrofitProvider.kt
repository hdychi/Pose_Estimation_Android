package com.hdychi.pose

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

object RetrofitProvider {
    private const val BASE_URL = "http://172.23.242.149:8000/api/"

    val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(120, TimeUnit.MINUTES)
        .readTimeout(120, TimeUnit.MINUTES)
        .writeTimeout(120, TimeUnit.MINUTES)
        .retryOnConnectionFailure(true)
        .build()

    val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())


    val rawRetrofit = builder.build()

    val gsonRetrofit = builder.addConverterFactory(GsonConverterFactory.create()).build()

    fun <T> create(service: Class<T>, isGson: Boolean = false): T = when (isGson) {
        true -> gsonRetrofit.create(service)
        false -> rawRetrofit.create(service)
    }
}