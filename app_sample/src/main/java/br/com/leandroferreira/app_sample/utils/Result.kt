package br.com.leandroferreira.app_sample.utils

sealed class ResultData<T> {

    class Idle<T>: ResultData<T>()
    class Loading<T> : ResultData<T>()
    class Complete<T>(val data: T): ResultData<T>()
    class Error<T>(val exception: Exception) : ResultData<T>()
}
