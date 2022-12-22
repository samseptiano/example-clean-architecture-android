package com.example.pixabaygalleryapp.di.modules

import com.example.pixabaygalleryapp.data.repository.image.ImageDataSource
import com.example.pixabaygalleryapp.data.repository.image.ImageRepository
import com.example.pixabaygalleryapp.domain.service.ImageServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GeneralRepositoryModule {
    @Singleton
    @Provides
    fun provideGeneralDataSource(authApi: ImageServices): ImageDataSource {
        return ImageRepository(authApi)
    }

}