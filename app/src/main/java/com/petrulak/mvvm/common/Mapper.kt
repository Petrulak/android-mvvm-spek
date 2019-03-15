package com.petrulak.mvvm.common


abstract class Mapper<From, To> {

    abstract fun map(from: From): To

    fun map(list: List<From>?): List<To> {
        if (list != null) {
            val result = ArrayList<To>(list.size)
            list.mapTo(result) { map(it) }
            return result
        }
        return emptyList()
    }

    companion object {
        const val INVALID_INT = -1
        const val INVALID_LONG = -1L
        const val INVALID_DOUBLE = -1.0
        const val INVALID_FLOAT = -1.0f
        const val EMPTY_STRING = ""
    }
}