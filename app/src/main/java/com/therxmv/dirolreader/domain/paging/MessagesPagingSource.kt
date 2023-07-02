package com.therxmv.dirolreader.domain.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.therxmv.dirolreader.domain.models.MessageModel
import com.therxmv.dirolreader.domain.repository.MessageRepository
import com.therxmv.dirolreader.utils.PAGE_SIZE
import com.therxmv.dirolreader.utils.STARTING_PAGE_INDEX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.Client

class MessagesPagingSource(
    private val client: Client?,
    private val messageRepository: MessageRepository,
): PagingSource<Int, MessageModel>() {
    override fun getRefreshKey(state: PagingState<Int, MessageModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MessageModel> = withContext(Dispatchers.IO) {
        try {
            val pageIndex = params.key ?: STARTING_PAGE_INDEX
            val data = messageRepository.getMessagesByPage(client, pageIndex * params.loadSize, PAGE_SIZE)

//            Log.d("rozmi_paging", data.map { it.id }.toString())

            LoadResult.Page(
                data = groupPhotoMessages(data),
                prevKey = if (pageIndex == STARTING_PAGE_INDEX) null else pageIndex - 1,
                nextKey = if (data.isEmpty()) null else pageIndex + 1,
            )
        }
        catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    private fun groupPhotoMessages(list: List<MessageModel>): List<MessageModel> {
        val temp = mutableListOf<MessageModel>()

        if(list.isEmpty() || list.size == 1) return list

        var id = 0
        temp.add(list[id])

        for(i in 1 until list.size) {
            val prevItem = temp[id]
            val currentItem = list[i]

            if(prevItem.mediaList == null || currentItem.mediaList == null) {
                temp.add(currentItem)
                id++
            }
            else {
                if(currentItem.timestamp - prevItem.timestamp <= 10 && currentItem.channelId == prevItem.channelId) {
                    prevItem.mediaList.add(currentItem.mediaList[0])

                    prevItem.id = currentItem.id

                    if(currentItem.text.isNotEmpty()) {
                        prevItem.text = currentItem.text
                    }
                }
                else {
                    temp.add(currentItem)
                    id++
                }
            }
        }

        return temp
    }
}