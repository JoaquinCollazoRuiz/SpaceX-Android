package com.joaquincollazoruiz.spacex.repository

import com.joaquincollazoruiz.spacex.data.local.AppDB
import com.joaquincollazoruiz.spacex.data.local.model.toDomainModel
import com.joaquincollazoruiz.spacex.data.remote.IFetchLaunchesService
import com.joaquincollazoruiz.spacex.data.remote.model.*
import com.joaquincollazoruiz.spacex.domain.interfaces.ILaunchesRepository
import com.joaquincollazoruiz.spacex.domain.model.FilteringOption
import com.joaquincollazoruiz.spacex.domain.model.Launch
import com.joaquincollazoruiz.spacex.domain.model.LaunchStatus
import com.joaquincollazoruiz.spacex.domain.model.SortingOption
import com.joaquincollazoruiz.spacex.repository.caching.ICurrentUnixTimeProvider
import com.joaquincollazoruiz.spacex.repository.caching.LaunchesCachingConfig
import java.net.UnknownHostException
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.abs

class LaunchesRepository(
    private val launchesService: IFetchLaunchesService,
    private val db: AppDB,
    private val cachingConfig: LaunchesCachingConfig,
    private val currentUnixTimeProvider: ICurrentUnixTimeProvider
) : ILaunchesRepository {

    private var lastSuccessfulRemoteFetchTime: Long = 0L

    override suspend fun getAllLaunchesWithCriteria(
        sortingOption: SortingOption,
        filterStatusOption: FilteringOption.ByLaunchStatus?,
    ): List<Launch> {
        if (isCacheStillValid()) {
            return getFromDb(sortingOption, filterStatusOption)
        }
        return try {
            fetchFromRemote(sortingOption, filterStatusOption).also {
                lastSuccessfulRemoteFetchTime = currentUnixTimeProvider.currentTimeMillis()
            }
        } catch (e: UnknownHostException) {
            getFromDb(sortingOption, filterStatusOption)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private suspend fun fetchFromRemote(
        sortingOption: SortingOption,
        filterStatusOption: FilteringOption.ByLaunchStatus?,
    ): List<Launch> {
        val sortDirection = if (sortingOption is SortingOption.Ascending) 1 else -1
        val paginationResponse = launchesService.fetchLaunchesWithRockets(
            LaunchesWithRocketsRequestBody(
                options = RequestOptions(
                    sort = SortByDateUnixOption(
                        sortDirection
                    )
                )
            )
        )
        val launchDtos = paginationResponse.body()?.docs

        replaceAllDbEntries(launchDtos)

        val filteredLaunches = launchDtos
            ?.filter {
                when (filterStatusOption?.status) {
                    LaunchStatus.Successful -> it.success == true
                    LaunchStatus.Failed -> it.success == false
                    LaunchStatus.FutureLaunch -> it.success == null
                    null -> true
                }
            }
            ?.map { it.toDomainModel() }
            ?.toList()
        return filteredLaunches ?: emptyList()
    }

    private fun getFromDb(
        sortingOption: SortingOption,
        filterStatusOption: FilteringOption.ByLaunchStatus?,
    ): List<Launch> {
        val dao = db.launchesDao()
        val sortAscending = sortingOption == SortingOption.Ascending

        val launchStatusCriteria: Boolean? = when (filterStatusOption?.status) {
            LaunchStatus.Successful -> true
            LaunchStatus.Failed -> false
            else -> null
        }

        return dao.getAllWithMatchingCriteria(
            sortAscending,
            launchStatusCriteria = launchStatusCriteria,
        null)
            ?.map { it.toDomainModel() }
            ?.toList()
            ?: emptyList()
    }

    private fun replaceAllDbEntries(launchDtos: List<LaunchWithRocketDto>?) {
        if (!launchDtos.isNullOrEmpty()) {
            db.launchesDao().deleteExistingAndInsertAll(launches = launchDtos.map { it.toEntity() })
        }
    }

    private fun isCacheStillValid(): Boolean {
        val now = currentUnixTimeProvider.currentTimeMillis()
        val elapsed = abs(now - lastSuccessfulRemoteFetchTime)
        return elapsed <= cachingConfig.launchesCacheValidityInMillis
    }

}