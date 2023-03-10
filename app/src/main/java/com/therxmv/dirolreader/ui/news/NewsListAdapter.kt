package com.therxmv.dirolreader.ui.news

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.therxmv.dirolreader.data.repository.ClientRepositoryImpl
import com.therxmv.dirolreader.databinding.NewsPostBinding
import com.therxmv.dirolreader.domain.models.ChannelModel
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.Chat
import org.drinkless.td.libcore.telegram.TdApi.DownloadFile
import org.drinkless.td.libcore.telegram.TdApi.GetChat
import org.drinkless.td.libcore.telegram.TdApi.GetMessage
import org.drinkless.td.libcore.telegram.TdApi.GetMessages
import org.drinkless.td.libcore.telegram.TdApi.Message
import org.drinkless.td.libcore.telegram.TdApi.MessagePhoto
import org.drinkless.td.libcore.telegram.TdApi.MessageText
import java.io.File

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
        val client = ClientRepositoryImpl.client
        fun bind(item: ChannelModel?) = with(binding) {
            if(item != null) {
                postTimeAgo.text = "Few minutes ago"
//                timeToReadText.text = "Few minutes"

                client?.send(GetChat(item.id)) { c ->
                    c as Chat
                    channelName.text = c.title
                    channelName.isSelected = true

                    client.send(GetMessage(c.id, c.lastMessage?.id!!)) { m ->
                        m as Message

                        when(m.content) {
                            is MessageText -> {
                                postText.text = (m.content as MessageText).text.text
                                postImageViewCard.visibility = GONE
                            }
                            is MessagePhoto -> {
                                postText.text = (m.content as MessagePhoto).caption.text
                                postImageViewCard.visibility = VISIBLE

                                val photo = (m.content as MessagePhoto).photo.sizes[0].photo
                                if(photo.local.isDownloadingCompleted) {
                                    val bitmap = BitmapFactory.decodeFile(photo.local.path)
                                    postImageView.setImageBitmap(bitmap)
                                }
                                else {
                                    client.send(DownloadFile(photo.id, 32, 0, 1, true)) { f ->
                                        f as TdApi.File
                                        val bitmap = BitmapFactory.decodeFile(f.local.path)
                                        postImageView.setImageBitmap(bitmap)
                                    }
                                }
                            }
                            else -> {
                                postText.text = "Unsupported message type yet"
                                postImageViewCard.visibility = GONE
                            }
                        }
                    }

                    val photo = c.photo?.small
                    if(photo?.id != null) {
                        if (photo.local?.isDownloadingCompleted == true) {
                            val bitmap = BitmapFactory.decodeFile(c.photo?.small?.local?.path)
                            channelAvatarImageView.setImageBitmap(bitmap)
                        }
                        else {
                            client.send(DownloadFile(photo.id, 32, 0, 1, true)) { f ->
                                f as TdApi.File
                                val bitmap = BitmapFactory.decodeFile(f.local.path)
                                channelAvatarImageView.setImageBitmap(bitmap)
                            }
                        }
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