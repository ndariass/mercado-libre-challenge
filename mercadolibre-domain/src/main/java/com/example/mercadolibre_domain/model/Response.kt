package com.example.mercadolibre_domain.model

/**
 * Generic class to return responses from repository classses
 *
 * @author Nicolás Arias
 */
data class Response<T>(
    val payload: T?,
    val paging: Paging?,
    val successful: Boolean,
    val error: Error?,
    val errorMessage: String?
)

/**
 * Pagination information related to a response
 *
 * @author Nicolás Arias
 */
data class Paging(
    val totalElements: Int,
    val offset: Int,
    val pageSize: Int
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
