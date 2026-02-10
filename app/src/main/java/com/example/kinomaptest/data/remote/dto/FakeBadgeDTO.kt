package com.example.kinomaptest.data.remote.dto

import com.google.gson.annotations.SerializedName


data class FakeBadgeDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("action")
    val action: String,
    @SerializedName("category")
    val category: String,

    @SerializedName("unlocked_date")
    val unlockedDate: Long?,
    @SerializedName("unlocked_percent")
    val unlockedPercent: Int?,

    @SerializedName("images_url")
    val imagesUrl: ImageUrlDTO

)
