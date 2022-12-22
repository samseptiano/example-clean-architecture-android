package com.example.pixabaygalleryapp.base.usecases


import com.example.pixabaygalleryapp.base.coroutine.AppDispatchers
import com.example.pixabaygalleryapp.domain.core.base.model.Either
import com.example.pixabaygalleryapp.domain.core.base.model.Failure
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This abstraction represents an execution unit for different use cases (this means that any use
 * case in the application should implement this contract).
 *
 * By convention each [UseCase] implementation will execute its job in a background thread
 * (kotlin coroutine) and will post the result in the UI thread.
 *
 * adjusted from https://github.com/android10/Android-CleanArchitecture-Kotlin/
 */

abstract class UseCase<out Type, in Params> where Type : Any {

    abstract suspend fun run(params: Params): Flow<Type>

    operator fun invoke(
        params: Params,
        scope: CoroutineScope,
        appDispatchers: AppDispatchers,
        onResult: (Flow<Type>) -> Unit = {}
    ) {
        scope.launch {
            val deferred = async(appDispatchers.io) {
                run(params)
            }
            onResult(deferred.await())
        }
    }

    class None
}