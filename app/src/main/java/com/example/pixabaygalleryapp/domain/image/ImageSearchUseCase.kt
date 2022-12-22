package com.example.pixabaygalleryapp.domain.image

import com.example.pixabaygalleryapp.data.repository.image.ImageDataSource
import com.example.pixabaygalleryapp.base.usecases.UseCase
import com.example.pixabaygalleryapp.model.ImagesResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author SamuelSep on 4/20/2021.
 */
class ImageSearchUseCase @Inject constructor(private val repository: ImageDataSource) :
    UseCase<ImagesResult, ImageSearchUseCase.Params>() {

    data class Params(
        val page: Int = 1,
        val category: String
    )

    override suspend fun run(params: Params): Flow<ImagesResult> {
        return repository.getSearchImages(
            page = params.page,
            search = params.category
        )
    }
}