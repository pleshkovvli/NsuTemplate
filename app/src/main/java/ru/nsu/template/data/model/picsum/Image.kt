package ru.nsu.template.data.model.picsum

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Image(
        @SerializedName("id")
        val id: String,
        @SerializedName("author")
        val author: String,
        @SerializedName("width")
        val width: Int,
        @SerializedName("height")
        val height: Int,
        @SerializedName("url")
        val url: String,
        @SerializedName("download_url")
        val downloadUrl: String
) : Serializable