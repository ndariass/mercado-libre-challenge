package com.example.mercadolibre_data

import com.google.gson.Gson
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Utilites for unit testing
 *
 * @author Nicolás Arias
 */
object TestUtils {
    private val objectMapper = Gson()

    /**
     * Gets an object of type [T] by deserializing the content of the given file
     *
     * @param path the path to the file
     * @param clazz the target class to perform the deserialization
     * @return the deserialized object
     */
    fun <T> readFromFile(path: String, clazz: Class<T>): T {
        val json = String(Files.readAllBytes(Paths.get(javaClass.getResource(path)!!.file)))
        return readFromJson(json, clazz)
    }

    /**
     * Gets an object of type [T] by deserializing the content of the given JSON represented as a
     * String.
     *
     * @param json the json to be deserialized
     * @param clazz the target class to perform the deserialization
     * @return the deserialized object
     */
    fun <T> readFromJson(json: String, clazz: Class<T>): T =
        objectMapper.fromJson(json, clazz)
}
