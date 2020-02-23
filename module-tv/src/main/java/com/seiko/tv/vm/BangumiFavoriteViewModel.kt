package com.seiko.tv.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.seiko.tv.data.model.HomeImageBean
import com.seiko.tv.domain.bangumi.GetBangumiFavoriteFixedUseCase
import com.seiko.tv.domain.bangumi.GetBangumiFavoriteUseCase
import com.seiko.tv.domain.bangumi.GetBangumiHistoryFixedUseCase
import com.seiko.tv.domain.bangumi.GetBangumiHistoryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

class BangumiFavoriteViewModel(
    private val getBangumiHistoryList: GetBangumiFavoriteFixedUseCase
) : ViewModel() {

    /**
     * 我的历史，前20条
     */
    val favoriteBangumiList: LiveData<List<HomeImageBean>> =
        liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            delay(250)
            emit(getBangumiHistoryList.invoke())
        }
}