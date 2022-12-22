package com.example.pixabaygalleryapp.data.repository.image

import com.example.pixabaygalleryapp.domain.service.ImageServices
import com.example.pixabaygalleryapp.model.ImagesResult
import com.example.pixabaygalleryapp.utils.CONSTANTS.API_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * @author SamuelSep on 4/20/2021.
 */
class ImageRepository @Inject constructor(private val apiService: ImageServices) : ImageDataSource {

    override suspend fun getAllImages(
        page: Int,
        category: String
    ): Flow<ImagesResult> {
        return flow {
            emit(
                apiService.getImagesData(
                    key = API_KEY,
                    page = page,
                    category = category
                )
            )
        }
    }

    override suspend fun getSearchImages(page: Int, search: String): Flow<ImagesResult> {
        return flow {
            emit(
                apiService.getSearchImageData(
                    key = API_KEY,
                    page = page,
                    search = search
                )
            )
        }
    }

}