package com.therxmv.otaupdates.domain.models

data class LatestReleaseModel(
    val version: String,
    val changeLog: String,
    val fileName: String,
    val contentType: String,
    val downloadUrl: String,
)