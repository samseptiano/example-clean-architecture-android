package com.example.pixabaygalleryapp.ui.fragment.image

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pixabaygalleryapp.base.data.ResponseStatusCallbacks
import com.example.pixabaygalleryapp.base.viewmodel.BaseViewModel
import com.example.pixabaygalleryapp.domain.image.ImageSearchUseCase
import com.example.pixabaygalleryapp.domain.image.ImageUseCase
import com.example.pixabaygalleryapp.model.FetchDataModel
import com.example.pixabaygalleryapp.model.ImagesInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.apache.commons.lang3.StringUtils
import javax.inject.Inject

/**
 * @author SamuelSep on 4/20/2021.
 */

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val imageUseCase: ImageUseCase,
    private val imageSearchUseCase: ImageSearchUseCase
) : BaseViewModel(), Parcelable {

    private val _imagesList = MutableLiveData<ResponseStatusCallbacks<FetchDataModel>>()
    val imagesResponse: LiveData<ResponseStatusCallbacks<FetchDataModel>>
        get() = _imagesList

    private val _selectedImages = MutableLiveData<ResponseStatusCallbacks<ImagesInfo>>()
    val selectedImagesResponse: LiveData<ResponseStatusCallbacks<ImagesInfo>>
        get() = _selectedImages

    private var pagination = 1
    private var searchImage = StringUtils.EMPTY
    private var updatedItems = arrayListOf<ImagesInfo>()

    constructor(parcel: Parcel) : this(
        TODO("imageUseCase"),
        TODO("imageSearchUseCase")
    ) {
        pagination = parcel.readInt()
        searchImage = parcel.readString()?:""
    }

    init {
        fetchImagesFromRemoteServer(pagination)
    }

    /*
    * Observed function for initiate searching
    * */
    fun fetchImagesFromRemoteServer(pagination: Int) {
        _imagesList.value = ResponseStatusCallbacks.loading(
            data = FetchDataModel(
                page = pagination,
                imagesInfo = null
            )
        )
        viewModelScope.launch {
            try {
                imageUseCase.invoke(page = pagination).collect { dataset ->
                    dataset.imagesInfo.let {
                        if (it.isNotEmpty()) {
                            if (pagination == 1) {
                                updatedItems = arrayListOf()
                                updatedItems.addAll(it)
                            } else {
                                updatedItems.addAll(it)
                            }
                            _imagesList.postValue(
                                ResponseStatusCallbacks.success(
                                data = FetchDataModel(page = pagination, imagesInfo = updatedItems),
                                "Products received"
                            ))
                        } else
                            _imagesList.value = ResponseStatusCallbacks.error(
                                data = FetchDataModel(
                                    page = pagination,
                                    imagesInfo = null
                                ),
                                if (pagination == 1) "Sorry no images received" else "Sorry no more images available"
                            )
                    }

                }
            } catch (e: Exception) {
                _imagesList.value = ResponseStatusCallbacks.error(null, e.message.toString())
            }
        }
    }

    /*
    * Send data to detail page
    * */
    fun setSelectedProduct(singleImages: ImagesInfo) {
        _selectedImages.value = ResponseStatusCallbacks.loading(null)
        viewModelScope.launch {
            try {
                _selectedImages.value = ResponseStatusCallbacks.success(
                    data = singleImages,
                    "Image received"
                )
            } catch (e: Exception) {
                _imagesList.value = ResponseStatusCallbacks.error(null, e.message.toString())
            }
        }
    }

    /*
    * load next page
    * */
    fun loadNextPagePhotos() {
        pagination++
        if (searchImage == StringUtils.EMPTY) {
            fetchImagesFromRemoteServer(pagination)
        } else {
            fetchSearchImagesFromRemoteServer(pagination, searchImage)
        }

    }

    /*
    * Retry connection if internet is not available
    * */
    fun retryConnection() {
        if (searchImage == StringUtils.EMPTY) {
            fetchImagesFromRemoteServer(pagination)
        } else {
            fetchSearchImagesFromRemoteServer(pagination, searchImage)
        }
    }

    /*
    * Search function for searching photos by name
    * */
    fun searchImagesFromRemote(search: String) {
        pagination = 1
        searchImage = search
        fetchSearchImagesFromRemoteServer(pagination, search)
    }

    /*
    * Query to fetch images from server
    * */
    private fun fetchSearchImagesFromRemoteServer(pagination: Int, search: String) {
        _imagesList.value = ResponseStatusCallbacks.loading(
            data = FetchDataModel(
                page = pagination,
                imagesInfo = null
            )
        )
        viewModelScope.launch {
            try {
                val params = ImageSearchUseCase.Params(page = pagination, category = search)
                imageSearchUseCase.run(params).collect { dataset ->
                    dataset.imagesInfo.let {
                        if (it.isNotEmpty()) {
                            if (pagination == 1) {
                                updatedItems = arrayListOf()
                                updatedItems.addAll(it)
                            } else {
                                updatedItems.addAll(it)
                            }
                            _imagesList.postValue(
                                ResponseStatusCallbacks.success(
                                data = FetchDataModel(page = pagination, imagesInfo = updatedItems),
                                "Products received"
                            ))
                        } else
                            _imagesList.value = ResponseStatusCallbacks.error(
                                data = FetchDataModel(
                                    page = pagination,
                                    imagesInfo = null
                                ),
                                if (pagination == 1) "Sorry no images received" else "Sorry no more images available"
                            )
                    }

                }
            } catch (e: Exception) {
                _imagesList.value = ResponseStatusCallbacks.error(null, e.message.toString())
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(pagination)
        parcel.writeString(searchImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageViewModel> {
        override fun createFromParcel(parcel: Parcel): ImageViewModel {
            return ImageViewModel(parcel)
        }

        override fun newArray(size: Int): Array<ImageViewModel?> {
            return arrayOfNulls(size)
        }
    }

}