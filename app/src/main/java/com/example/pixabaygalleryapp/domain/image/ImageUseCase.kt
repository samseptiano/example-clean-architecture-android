package com.example.pixabaygalleryapp.domain.image

import com.example.pixabaygalleryapp.data.repository.image.ImageDataSource
import javax.inject.Inject

/**
 * @author SamuelSep on 4/20/2021.
 */
class ImageUseCase @Inject constructor(private val repository: ImageDataSource) {
    suspend operator fun invoke(
        page: Int = 1,
        category: String = "latest"
    ) = repository.getAllImages(
        page = page,
        category = category
    )
}