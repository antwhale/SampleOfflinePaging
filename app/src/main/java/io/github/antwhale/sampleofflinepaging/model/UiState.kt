package io.github.antwhale.sampleofflinepaging.model

sealed class UiState<out T>(val baseData: T?) {
    data object Loading : UiState<Nothing>(baseData = null)
    data object Error : UiState<Nothing>(baseData = null)
    data class Success<out R>(val result: R) : UiState<R>(baseData = result)
}