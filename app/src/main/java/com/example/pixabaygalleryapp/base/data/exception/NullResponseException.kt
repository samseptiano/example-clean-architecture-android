package com.example.pixabaygalleryapp.base.data.exception

/**
 * NullResponseException is thrown when api response "record" field is null
 * No need message
 * Can be replaced with data using ResultState<T>.replaceIfNull()
 * Action/Message must be determined in UI layer
 */
class NullResponseException: Exception()