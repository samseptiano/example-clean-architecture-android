package com.example.pixabaygalleryapp.domain.service

import com.example.pixabaygalleryapp.model.ImagesResult
import com.example.pixabaygalleryapp.utils.CONSTANTS
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author SamuelSep on 4/20/2021.
 */
interface ImageServices {

    @GET(ImageRoutes.GET_IMAGES_DATA)
    suspend fun getImagesData(
        @Query(CONSTANTS.KEY) key: String,
        @Query(CONSTANTS.PAGE) page: Int = 1,
        @Query(CONSTANTS.CATEGORY) category: String = "latest"
    ): ImagesResult

    @GET(ImageRoutes.GET_IMAGES_DATA)
    suspend fun getSearchImageData(
        @Query(CONSTANTS.KEY) key: String,
        @Query(CONSTANTS.PAGE) page: Int = 1,
        @Query(CONSTANTS.SEARCH) search: String
    ): ImagesResult

}