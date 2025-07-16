package com.cryptotracker.core

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    /**
     * Represents successful state
     */
    class Success<T>(data: T) : Resource<T>(data)

    /**
     * Represents error state
     */
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    /**
     * Represents loading state
     */
    class Loading<T>(data: T? = null) : Resource<T>(data)
}