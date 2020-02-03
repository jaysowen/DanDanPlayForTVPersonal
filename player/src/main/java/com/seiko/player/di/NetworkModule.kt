package com.seiko.player.di

import com.seiko.player.data.api.DanDanCommentApiGenerator
import com.seiko.player.data.api.DanDanCommentApiService
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Converter

internal val networkModule = module {
    single { createDanDanCommentApiService(get(), get()) }
}

private fun createDanDanCommentApiService(okHttpClient: OkHttpClient, converterFactory: Converter.Factory): DanDanCommentApiService {
    return DanDanCommentApiGenerator(okHttpClient, converterFactory).create()
}