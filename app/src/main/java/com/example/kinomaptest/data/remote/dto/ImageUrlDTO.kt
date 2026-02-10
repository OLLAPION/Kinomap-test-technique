package com.example.kinomaptest.data.remote.dto

import com.google.gson.annotations.SerializedName


data class ImageUrlDTO(
    @SerializedName("unlocked")
    val unlocked: String,
    @SerializedName("locked")
    val locked: String
)