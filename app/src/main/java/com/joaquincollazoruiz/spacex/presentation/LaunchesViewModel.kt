package com.joaquincollazoruiz.spacex.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joaquincollazoruiz.spacex.R
import com.joaquincollazoruiz.spacex.domain.model.FilteringOption
import com.joaquincollazoruiz.spacex.domain.model.Launch
import com.joaquincollazoruiz.spacex.domain.model.SortingOption
import com.joaquincollazoruiz.spacex.domain.usecase.IGetLaunchesUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LaunchesViewModel(
    private val getLaunchesUseCase: IGetLaunchesUseCase,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    sealed interface UiState {
        object Loading : UiState
        data class Display(val launches: List<Launch>) : UiState

        data class Error(val errorRes: Int) : UiState
    }

    sealed interface UserAction {
        object Refresh : UserAction
        data class SelectSortingPreference(
            val sortingOption: SortingOption,
        ) : UserAction

        data class SelectStatusFilteringPreference(
            val filteringOption: FilteringOption.ByLaunchStatus?,
        ) : UserAction
    }

    private val _uiState: MutableLiveData<UiState> = MutableLiveData(UiState.Loading)
    val uiState: LiveData<UiState> get() = _uiState

    private val _sortingPreference: MutableLiveData<SortingOption> =
        MutableLiveData(SortingOption.Descending)
    val sortingPreference: LiveData<SortingOption> get() = _sortingPreference

    private val _filterStatusPreference: MutableLiveData<FilteringOption.ByLaunchStatus> =
        MutableLiveData(null)
    val filterStatusPreference: LiveData<FilteringOption.ByLaunchStatus> get() = _filterStatusPreference

    init {
        handleUserAction(UserAction.Refresh)
    }

    fun handleUserAction(action: UserAction) {
        when (action) {
            is UserAction.SelectSortingPreference -> _sortingPreference.value = action.sortingOption
            UserAction.Refresh -> {}
            is UserAction.SelectStatusFilteringPreference -> {
                _filterStatusPreference.value = action.filteringOption
            }
        }
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch(defaultDispatcher) {
            getLaunchesUseCase.execute(
                _sortingPreference.value!!,
                _filterStatusPreference.value,
            ).also(
                this@LaunchesViewModel::handleLaunchesResult
            )
        }
    }

    private fun handleLaunchesResult(result: IGetLaunchesUseCase.Result) {
        when (result) {
            is IGetLaunchesUseCase.Result.Error -> _uiState.postValue(UiState.Error(R.string.unknown_error_message))
            is IGetLaunchesUseCase.Result.Success -> _uiState.postValue(UiState.Display(result.launches))
        }
    }
}