package com.joaquincollazoruiz.spacex.domain.usecase

import com.joaquincollazoruiz.spacex.domain.model.FilteringOption
import com.joaquincollazoruiz.spacex.domain.model.Launch
import com.joaquincollazoruiz.spacex.domain.model.SortingOption

interface IGetLaunchesUseCase {

    suspend fun execute(
        sortingOption: SortingOption = SortingOption.Descending,
        filterStatusOption: FilteringOption.ByLaunchStatus? = null,
    ): Result

    sealed interface Result {
        data class Success(val launches: List<Launch>) : Result

        data class Error(val t: Throwable) : Result
    }
}