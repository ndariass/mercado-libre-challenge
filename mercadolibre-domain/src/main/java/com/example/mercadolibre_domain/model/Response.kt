package com.example.mercadolibre_domain.model

import org.omg.PortableInterceptor.SUCCESSFUL

/**
 * Generic class to return responses from repository classses
 *
 * @author Nicolás Arias
 */
data class Response<T>(
    val payload: T?,
    val successful: Boolean,
    val errorMessage: String?
)
