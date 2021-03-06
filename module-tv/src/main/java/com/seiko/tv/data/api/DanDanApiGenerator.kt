package com.seiko.tv.data.api

import android.content.Context
import com.seiko.tv.util.constants.DANDAN_API_BASE_URL
import com.seiko.tv.data.prefs.PrefDataSource
import com.seiko.tv.util.http.cookie.CookiesManager
import com.seiko.tv.util.http.interceptor.RetrofitCacheInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit

internal class DanDanApiGenerator(
    context: Context,
    okHttpClient: OkHttpClient,
    converterFactory: Converter.Factory,
    cookiesManager: CookiesManager,
    prefDataSource: PrefDataSource
) {
    private val newOkHttpClient = okHttpClient.newBuilder()
        .cookieJar(cookiesManager)
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .addInterceptor(RetrofitCacheInterceptor(context))
        .addInterceptor { chain ->
            val token = prefDataSource.token
            if (token.isNotEmpty()) {
                val original = chain.request()
                val builder = original.newBuilder()
                    .header("Authorization", "Bearer $token")
                return@addInterceptor chain.proceed(builder.build())
            }
            return@addInterceptor chain.proceed(chain.request())
        }
//        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(DANDAN_API_BASE_URL)
        .callFactory(newOkHttpClient.build())
        .addConverterFactory(converterFactory)

    fun create(): DanDanApiService {
        return retrofit.build().create(DanDanApiService::class.java)
    }
}