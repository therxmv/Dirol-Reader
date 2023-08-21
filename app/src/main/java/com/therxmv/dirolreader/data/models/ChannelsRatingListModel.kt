package com.therxmv.dirolreader.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChannelsRatingListModel(
    val list: MutableList<ChannelRatingModel> = mutableListOf()
) : Parcelable

@Parcelize
data class ChannelRatingModel(
    val channelId: Long,
    val rating: Int,
): Parcelable