package com.therxmv.otaupdates.data.models

import com.google.gson.annotations.SerializedName
import com.therxmv.otaupdates.domain.models.LatestReleaseModel

data class LatestReleaseJson(
    @SerializedName("name") val version: String,
    @SerializedName("body") val changeLog: String,
    @SerializedName("assets") val assets: List<LatestReleaseAssetJson>,
)

data class LatestReleaseAssetJson(
    @SerializedName("name") val fileName: String,
    @SerializedName("content_type") val contentType: String,
    @SerializedName("browser_download_url") val downloadUrl: String,
)

fun LatestReleaseJson.toDomain() = LatestReleaseModel(
    version,
    changeLog,
    assets.first().fileName,
    assets.first().contentType,
    assets.first().downloadUrl,
)