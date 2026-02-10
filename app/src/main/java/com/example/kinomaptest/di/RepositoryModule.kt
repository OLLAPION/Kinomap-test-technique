package com.example.kinomaptest.di

import com.example.kinomaptest.data.repository.BadgeRepositoryImpl
import com.example.kinomaptest.domain.repository.BadgeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindBadgeRepository(
        impl: BadgeRepositoryImpl
    ): BadgeRepository
}