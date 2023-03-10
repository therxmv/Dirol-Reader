package com.therxmv.dirolreader.data.source.locale

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.therxmv.dirolreader.data.source.locale.model.ChannelLocaleMapper
import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.utils.PAGE_SIZE
import com.therxmv.dirolreader.utils.STARTING_PAGE_INDEX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChannelLocaleDataSource(
    private val channelDao: ChannelDao,
) : PagingSource<Int, ChannelModel>() {
    fun addChannel(channelModel: ChannelModel) {
        channelDao.addChannel(ChannelLocaleMapper().mapToEntity(channelModel))
    }

    override fun getRefreshKey(state: PagingState<Int, ChannelModel>): Int? {
        // We need to get the previous key (or next key if previous is null) of the page
        // that was closest to the most recently accessed index.
        // Anchor position is the most recently accessed index.
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ChannelModel> =
        withContext(Dispatchers.IO) {
            try {
                val pageIndex = params.key ?: STARTING_PAGE_INDEX

                val response = channelDao.getChannelsByPage(PAGE_SIZE, pageIndex * params.loadSize).map {
                    ChannelLocaleMapper().mapFromEntity(it)
                }
//                Log.d("rozmi", (pageIndex * params.loadSize).toString())
//                Log.d("rozmi", "List1: ${response}")

                LoadResult.Page(
                    data = response,
                    prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex - 1,
                    nextKey = if (response.isEmpty()) null else pageIndex + 1,
                )
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
                LoadResult.Error(e)
            }
        }
}