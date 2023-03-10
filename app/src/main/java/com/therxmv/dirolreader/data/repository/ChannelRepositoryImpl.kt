package com.therxmv.dirolreader.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.therxmv.dirolreader.data.source.locale.ChannelLocaleDataSource
import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.models.NewsPostModel
import com.therxmv.dirolreader.domain.repository.ChannelRepository
import com.therxmv.dirolreader.utils.PAGE_SIZE
import kotlinx.coroutines.flow.Flow

class ChannelRepositoryImpl(
    private val channelLocaleDataSource: ChannelLocaleDataSource
): ChannelRepository {
    override fun getChannelsByPage(): Flow<PagingData<ChannelModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
//                prefetchDistance = PAGE_SIZE,
//                initialLoadSize = PAGE_SIZE * 2,
//                maxSize = Int.MAX_VALUE,
//                jumpThreshold = Int.MIN_VALUE
            ),
            pagingSourceFactory = {
                channelLocaleDataSource
            }
        ).flow
    }

    override fun addChannel(channelModel: ChannelModel) {
        channelLocaleDataSource.addChannel(channelModel)
    }
}