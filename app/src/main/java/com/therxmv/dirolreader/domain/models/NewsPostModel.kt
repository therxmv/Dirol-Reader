package com.therxmv.dirolreader.domain.models

data class NewsPostModel(
    val id: Long,
    val avatar: Int?,
    val title: String,
    val timeAgo: String,
    val message: String,
    val photos: List<Int>?,
    val readTime: String,
)
