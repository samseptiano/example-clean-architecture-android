package com.example.pixabaygalleryapp.utils

/**
 * @author SamuelSep on 9/7/2021.
 */

object Keys {

    init {
        System.loadLibrary("native-lib")
    }

    external fun apiKey(): String
}