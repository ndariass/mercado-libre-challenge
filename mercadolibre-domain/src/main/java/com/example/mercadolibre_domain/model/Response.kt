package com.example.mercadolibre_domain.model

/**
 * Generic class to return responses from repository classses
 *
 * @author Nicolás Arias
 */
data class Response<T>(
    val payload: T?,
    val successful: Boolean,
    val error: Error?,
    val errorMessage: String?
)

/**
 * Enum class with the possible error types
 *
 * @author Nicolás Arias
 */
enum class Error {
    NOT_FOUND,
    NETWORK_ERROR,
    GENERAL_ERROR
}
