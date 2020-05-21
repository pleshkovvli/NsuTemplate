package ru.nsu.template.data.network.picsum

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object PicsumApiClient {
    private const val REQUEST_TIMEOUT = 60
    private const val BASE_URL = "https://picsum.photos/"

    private var inited = false

    lateinit var retrofit: Retrofit
    lateinit var okHttpClient: OkHttpClient

    fun getClient(): Retrofit {
        if (!inited) {
            initOkHttp()

            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            inited = true
        }

        return retrofit
    }

    fun getImageUrl(id: Int, width: Int, height: Int): String = "${BASE_URL}id/$id/$width/$height"

    private fun initOkHttp() {
        val httpClient = OkHttpClient().newBuilder()
                .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)

        httpClient.addInterceptor(Interceptor { chain ->
            val request = chain.request()
            chain.proceed(request)
        })

        okHttpClient = httpClient.build()
    }
}