package com.therxmv.dirolreader.ui.news

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.therxmv.dirolreader.data.repository.ClientRepositoryImpl
import com.therxmv.dirolreader.databinding.NewsPostBinding
import com.therxmv.dirolreader.domain.models.ChannelModel
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.DownloadFile

class NewsListAdapter: PagingDataAdapter<ChannelModel, NewsListAdapter.NewsViewHolder>(NewsDiffCallBack()) {
    class NewsDiffCallBack: DiffUtil.ItemCallback<ChannelModel>() {
        override fun areItemsTheSame(oldItem: ChannelModel, newItem: ChannelModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ChannelModel, newItem: ChannelModel): Boolean {
            return oldItem.id == newItem.id
        }
    }

    class NewsViewHolder(private val binding: NewsPostBinding): RecyclerView.ViewHolder(binding.root) {
        private val client = ClientRepositoryImpl.client
        fun bind(item: ChannelModel?) = with(binding) {
            if(item != null) {
                channelAvatarImageView.setImageResource(0)
                postImageViewCard.visibility = GONE
                postTimeAgo.text = "Few minutes ago"
//                timeToReadText.text = "Few minutes"
                channelName.text = item.channelName
                postText.text = item.lastMessageText

                if(item.avatarPath != null) {
                    client?.send(DownloadFile(item.avatarPath, 22, 0, 1, true)) { f ->
                        f as TdApi.File
                        val bitmap = BitmapFactory.decodeFile(f.local.path)
                        channelAvatarImageView.setImageBitmap(bitmap)
                    }
                }

                if(item.photoPath != null) {
                    postImageViewCard.visibility = VISIBLE
                    client?.send(DownloadFile(item.photoPath, 32, 0, 1, true)) { f ->
                        f as TdApi.File
                        val bitmap = BitmapFactory.decodeFile(f.local.path)
                        postImageView.setImageBitmap(bitmap)
                    }
                }
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