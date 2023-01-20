package com.flow.moviesearchflowhomework.presentation.util

sealed class UiState<out T> {   // Use Sealed Class
    object Init : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val error: String) : UiState<Nothing>()
}
