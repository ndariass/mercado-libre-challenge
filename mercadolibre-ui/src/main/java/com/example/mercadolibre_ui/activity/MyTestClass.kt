package com.example.mercadolibre_ui.activity

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

/**
 *
 *
 * @author Nicolás Arias
 */
@Singleton
class MyTestClass @Inject constructor() {

    fun method1() {
        Log.d(javaClass.name, "Print something - 1")
    }
}

class MyTestClass2 () {

    fun method1() {
        Log.d(javaClass.name, "Print something - 2")
    }
}
