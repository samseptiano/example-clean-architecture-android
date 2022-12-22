package com.example.pixabaygalleryapp.base.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pixabaygalleryapp.data.repository.image.ImageRepository
import com.example.pixabaygalleryapp.ui.fragment.image.ImageViewModel
import com.example.pixabaygalleryapp.domain.image.ImageSearchUseCase
import com.example.pixabaygalleryapp.domain.image.ImageUseCase
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author SamuelSep on 4/20/2021.
 */
@Singleton
@Suppress("UNCHECKED_CAST")
class ViewModelFactory @Inject constructor(private val repository: ImageRepository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ImageViewModel::class.java) -> ImageViewModel(
                ImageUseCase(repository),
                ImageSearchUseCase(repository)
            ) as T
            else -> throw IllegalArgumentException("Unknown viewModel class $modelClass")
        }
    }

}