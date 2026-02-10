package com.example.kinomaptest.di

import com.example.kinomaptest.data.local.dao.BadgeDAO
import com.example.kinomaptest.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class DAOModule {

    @Provides
    fun provideBadgeDAO(appDatabase: AppDatabase): BadgeDAO {
        return appDatabase.badgeDAO()
    }
}