package com.therxmv.dirolreader.domain.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.therxmv.common.Paging.STARTING_PAGE_INDEX
import com.therxmv.dirolreader.domain.models.MessageModel
import com.therxmv.dirolreader.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MessagesPagingSource(
    private val messageRepository: MessageRepository,
    private val ioDispatcher: CoroutineDispatcher,
) : PagingSource<Int, MessageModel>() {

    override fun getRefreshKey(state: PagingState<Int, MessageModel>) =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MessageModel> =
        withContext(ioDispatcher) {
            try {
                val pageIndex = params.key ?: STARTING_PAGE_INDEX
                val data = messageRepository.getUnreadMessagesByPage(
                    page = pageIndex,
                )

//                Log.d("rozmi_paging", data.map { it.id }.toString())

                LoadResult.Page(
                    data = data,
                    prevKey = (pageIndex - 1).takeIf { pageIndex != STARTING_PAGE_INDEX },
                    nextKey = (pageIndex + 1).takeIf { data.isNotEmpty() },
                )
            } catch (e: Exception) {
                e.printStackTrace()
                LoadResult.Error(e)
            }
        }
}