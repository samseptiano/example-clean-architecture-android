package com.example.pixabaygalleryapp.data.repository.image

import com.example.pixabaygalleryapp.model.ImagesResult
import kotlinx.coroutines.flow.Flow

/**
 * @author SamuelSep on 4/20/2021.
 */
interface ImageDataSource {

    /*
    * Get all images from server
    * */
    suspend fun getAllImages(
        page: Int,
        category: String
    ): Flow<ImagesResult>
    /*
    * Get all images from server End
    * */

    /*
    * Get search images
    * */
    suspend fun getSearchImages(
        page: Int,
        search: String
    ): Flow<ImagesResult>
    /*
    * Get search images End
    * */


}