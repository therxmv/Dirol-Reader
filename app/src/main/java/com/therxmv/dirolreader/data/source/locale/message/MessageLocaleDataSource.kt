package com.therxmv.dirolreader.data.source.locale.message

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.therxmv.dirolreader.data.source.locale.DirolDao
import com.therxmv.dirolreader.data.source.locale.message.models.MessageLocaleMapper
import com.therxmv.dirolreader.domain.models.MessageModel
import com.therxmv.dirolreader.domain.models.PostModel
import com.therxmv.dirolreader.utils.PAGE_SIZE
import com.therxmv.dirolreader.utils.STARTING_PAGE_INDEX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MessageLocaleDataSource(
    private val dirolDao: DirolDao,
): PagingSource<Int, PostModel>() {
    fun addMessage(messageModel: MessageModel) {
        dirolDao.addMessage(MessageLocaleMapper().mapToEntity(messageModel))
    }

    suspend fun deleteMessage(messageModel: MessageModel) = withContext(Dispatchers.IO) {
        dirolDao.deleteMessage(MessageLocaleMapper().mapToEntity(messageModel))
    }

    override fun getRefreshKey(state: PagingState<Int, PostModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PostModel> = withContext(Dispatchers.IO) {
        try {
            val pageIndex = params.key ?: STARTING_PAGE_INDEX

            val postsList = dirolDao.getMessagesByPage(PAGE_SIZE, pageIndex * params.loadSize)

            val response =
                if (postsList.isNotEmpty()) {
                val newPostsList = postsList.filter { it.isNew }
//                Log.d("rozmi", newPostsList.toString())
                val oldPostsList = postsList.filter { !it.isNew }
                // TODO maybe add empty model with "All message read"
                newPostsList + oldPostsList
            }
            else {
                postsList
            }

//            Log.d("rozmi", response.toString())

            LoadResult.Page(
                data = response,
                prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex - 1,
                nextKey = if (response.isEmpty()) null else pageIndex + 1,
            )
        }
        catch (e: java.lang.Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}