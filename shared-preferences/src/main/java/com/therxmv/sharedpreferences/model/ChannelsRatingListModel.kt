package com.therxmv.sharedpreferences.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChannelsRatingListModel(
    val list: MutableList<ChannelRatingModel> = mutableListOf(),
) : Parcelable

@Parcelize
data class ChannelRatingModel(
    val channelId: Long,
    val rating: Int,
) : Parcelable