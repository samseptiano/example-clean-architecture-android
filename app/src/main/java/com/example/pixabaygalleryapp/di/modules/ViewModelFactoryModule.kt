package com.example.pixabaygalleryapp.di.modules

import androidx.lifecycle.ViewModelProvider
import com.example.pixabaygalleryapp.base.factory.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
interface ViewModelFactoryModule {
    @Binds
    fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}