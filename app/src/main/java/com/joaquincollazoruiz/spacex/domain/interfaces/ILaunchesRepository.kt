package com.joaquincollazoruiz.spacex.domain.interfaces

import com.joaquincollazoruiz.spacex.domain.model.FilteringOption
import com.joaquincollazoruiz.spacex.domain.model.Launch
import com.joaquincollazoruiz.spacex.domain.model.SortingOption

interface ILaunchesRepository {

    suspend fun getAllLaunchesWithCriteria(
        sortingOption: SortingOption,
        filterStatusOption: FilteringOption.ByLaunchStatus? = null,
    ): List<Launch>
}