package com.therxmv.otaupdates.data.models

import com.google.gson.annotations.SerializedName

data class LatestReleaseModel(
    @SerializedName("name") val version: String,
    @SerializedName("body") val changeLog: String,
    @SerializedName("assets") val assets: List<LatestReleaseAsset>
)

data class LatestReleaseAsset(
    @SerializedName("name") val fileName: String,
    @SerializedName("content_type") val contentType: String,
    @SerializedName("browser_download_url") val downloadUrl: String,
)
