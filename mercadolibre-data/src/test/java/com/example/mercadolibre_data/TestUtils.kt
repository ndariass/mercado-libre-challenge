package com.example.mercadolibre_data

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File

object TestUtils {
    private val objectMapper = ObjectMapper().apply {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
        setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
    }

    fun <T> readFromFile(path: String, clazz: Class<T>): T {
        val file = File(javaClass.getResource(path).file)
        return objectMapper.readValue(file, clazz)
    }
}
