package com.guilherme.knowyourfan.domain

interface Error

sealed class Result<out S, out E: Error> {
    data class Success<out S>(val data: S) : Result<S, Nothing>()
    data class Error<out E: com.guilherme.knowyourfan.domain.Error>(val error: E) : Result<Nothing, E>()
    data object Loading : Result<Nothing, Nothing>()

    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    val isLoading: Boolean get() = this is Loading

    fun getOrNull(): S? = when (this) {
        is Success -> data
        is Error, is Loading -> null
    }

    fun errorOrNull(): E? = when (this) {
        is Success, is Loading -> null
        is Error -> error
    }

    inline fun onSuccess(onSuccess: (S) -> Unit): Result<S, E> {
        if (this is Success) {
            onSuccess(data)
        }
        return this
    }

    inline fun onError(onError: (E) -> Unit): Result<S, E> {
        if (this is Error) {
            onError(error)
        }
        return this
    }

    inline fun onLoading(onLoading: () -> Unit): Result<S, E> {
        if (this is Loading) {
            onLoading()
        }
        return this
    }
}