package com.example.mercadolibre_data

import com.google.gson.Gson
import java.nio.file.Files
import java.nio.file.Paths

object TestUtils {
    private val objectMapper = Gson()

    fun <T> readFromFile(path: String, clazz: Class<T>): T {
        val json = String(Files.readAllBytes(Paths.get(javaClass.getResource(path)!!.file)))
        return readFromJson(json, clazz)
    }

    fun <T> readFromJson(json: String, clazz: Class<T>): T =
        objectMapper.fromJson(json, clazz)
}
