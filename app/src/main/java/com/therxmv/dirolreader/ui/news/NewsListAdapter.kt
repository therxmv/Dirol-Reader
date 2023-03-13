package com.therxmv.dirolreader.ui.news

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.therxmv.dirolreader.data.repository.ClientRepositoryImpl
import com.therxmv.dirolreader.databinding.NewsPostBinding
import com.therxmv.dirolreader.domain.models.MessageModel
import com.therxmv.dirolreader.domain.models.PostModel
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.DownloadFile

class NewsListAdapter(
    private val deleteMessage: (messageModel: MessageModel) -> Unit,
): PagingDataAdapter<PostModel, NewsListAdapter.NewsViewHolder>(NewsDiffCallBack()) {
    class NewsDiffCallBack: DiffUtil.ItemCallback<PostModel>() {
        override fun areItemsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PostModel, newItem: PostModel): Boolean {
            return oldItem.id == newItem.id
        }
    }

    inner class NewsViewHolder(private val binding: NewsPostBinding): RecyclerView.ViewHolder(binding.root) {
        private val client = ClientRepositoryImpl.client
        fun bind(item: PostModel?) = with(binding) {
            if(item != null) {
                channelAvatarImageView.setImageResource(0)
                postImageViewCard.visibility = GONE

                postTimeAgo.text = "Few minutes ago" // TODO calculate time
                channelName.text = item.title
                postText.text = item.text

                // TODO load avatar and photos
                if(item.avatarId != null) {
                    client?.send(DownloadFile(item.avatarId, 20, 0, 1, true)) { f ->
                        f as TdApi.File
                        val bitmap = BitmapFactory.decodeFile(f.local.path)
                        channelAvatarImageView.setImageBitmap(bitmap)
                    }
                }

//                if(item.avatarPath != null) {
//                    client?.send(DownloadFile(item.avatarPath, 22, 0, 1, true)) { f ->
//                        f as TdApi.File
//                        val bitmap = BitmapFactory.decodeFile(f.local.path)
//                        channelAvatarImageView.setImageBitmap(bitmap)
//                    }
//                }
//
//                if(item.photoPath != null) {
//                    postImageViewCard.visibility = VISIBLE
//                    client?.send(DownloadFile(item.photoPath, 32, 0, 1, true)) { f ->
//                        f as TdApi.File
//                        val bitmap = BitmapFactory.decodeFile(f.local.path)
//                        postImageView.setImageBitmap(bitmap)
//                    }
//                }

                deleteMessage(
                    MessageModel(
                        item.id,
                        item.messageThreadId,
                        item.channelId,
                        item.date,
                        item.text,
                        item.photoPath,
                        item.isLast,
                        item.isNew,
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }
}