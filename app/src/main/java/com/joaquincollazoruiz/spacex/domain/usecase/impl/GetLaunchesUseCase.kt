package com.joaquincollazoruiz.spacex.domain.usecase.impl

import com.joaquincollazoruiz.spacex.domain.interfaces.ILaunchesRepository
import com.joaquincollazoruiz.spacex.domain.model.FilteringOption
import com.joaquincollazoruiz.spacex.domain.model.SortingOption
import com.joaquincollazoruiz.spacex.domain.usecase.IGetLaunchesUseCase
import com.joaquincollazoruiz.spacex.domain.usecase.IGetLaunchesUseCase.Result

class GetLaunchesUseCase(private val launchesRepository: ILaunchesRepository) :
    IGetLaunchesUseCase {
    override suspend fun execute(
        sortingOption: SortingOption,
        filterStatusOption: FilteringOption.ByLaunchStatus?,
    ): Result {
        return try {
            return Result.Success(
                launchesRepository.getAllLaunchesWithCriteria(
                    sortingOption,
                    filterStatusOption,
                )
            )
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}