package com.flow.moviesearchflowhomework.data.util

interface BaseMapper<From, To> {
    fun map(from: From): To
}