package com.example.kinomaptest.data.remote.api

import com.example.kinomaptest.data.remote.dto.BadgeApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface FakeBadgeApiService {
    @GET("v4/badges/mobile-tech-test")
    suspend fun getBadges(
        @Query("appToken") appToken: String
    ): BadgeApiResponse
}