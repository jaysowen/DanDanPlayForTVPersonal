package com.dandanplay.tv.domain

import com.dandanplay.tv.data.model.HomeImageBean
import com.dandanplay.tv.util.toHomeImageBean
import com.seiko.common.data.Result
import com.dandanplay.tv.repo.BangumiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject

class GetFavoriteBangumiListUseCase : KoinComponent{

    private val bangumiRepository: BangumiRepository by inject()

    suspend operator fun invoke(): Result<List<HomeImageBean>> {
        return withContext(Dispatchers.Default) {
            return@withContext when(val result = bangumiRepository.getBangumiDetailsList()) {
                is Result.Success -> {
                    Result.Success(result.data.map { it.toHomeImageBean() })
                }
                is Result.Error -> Result.Error(result.exception)
            }
        }
    }

}