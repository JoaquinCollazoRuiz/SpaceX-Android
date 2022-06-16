package com.joaquincollazoruiz.spacex.repository

import com.joaquincollazoruiz.spacex.data.local.AppDB
import com.joaquincollazoruiz.spacex.data.local.model.LaunchEntity
import com.joaquincollazoruiz.spacex.data.remote.IFetchLaunchesService
import com.joaquincollazoruiz.spacex.data.remote.model.LaunchWithRocketDto
import com.joaquincollazoruiz.spacex.domain.model.*
import com.joaquincollazoruiz.spacex.repository.caching.ICurrentUnixTimeProvider
import com.joaquincollazoruiz.spacex.repository.caching.LaunchesCachingConfig
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import shared.fakes.*
import strikt.api.expectThat
import strikt.assertions.get
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
class LaunchesRepositoryTest {

    @MockK
    private lateinit var service: IFetchLaunchesService

    @MockK
    private lateinit var db: AppDB

    @MockK
    private lateinit var unixTimeProvider: ICurrentUnixTimeProvider

    private lateinit var launchesCachingConfig: LaunchesCachingConfig
    private lateinit var sortingOption: SortingOption

    // Subject
    private lateinit var launchesRepository: LaunchesRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        // Dependencies
        launchesCachingConfig = LaunchesCachingConfig(launchesCacheValidityInMillis = 0L)
        sortingOption = SortingOption.Ascending
        every { unixTimeProvider.currentTimeMillis() } returns 12345

        // Subject
        launchesRepository = LaunchesRepository(
            launchesService = service,
            db = db,
            cachingConfig = launchesCachingConfig,
            currentUnixTimeProvider = unixTimeProvider,
        )
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `getAllLaunchesWithCriteria should swallow general exception and return empty list when service throws`() {
        val errMsg = "fake error"
        coEvery { service.fetchLaunchesWithRockets(any()) } throws Exception(errMsg)

        runTest {
            val subject = launchesRepository.getAllLaunchesWithCriteria(
                sortingOption = sortingOption,
                null
            )

            expectThat(subject)
                .isEqualTo(listOf())
            coVerify(exactly = 1) {
                service.fetchLaunchesWithRockets(any())
                // ensure no DB lookup on general exception
                db wasNot Called
            }
        }
    }

    @Test
    fun `getAllLaunchesWithCriteria should save all launches to db but return filtered list matching criteria`() {
        val remoteLaunchesDtos: List<LaunchWithRocketDto> = listOf(
            FakeSuccessfulLaunchWithRocketDto2015,
            FakeSuccessfulLaunchWithRocketDto2016,
            FakeFailedLaunchWithRocketDto2015,
            FakeFailedLaunchWithRocketDto2016,
        )
        coEvery { service.fetchLaunchesWithRockets(any()).body()!!.docs } returns remoteLaunchesDtos
        val insertEntitiesListSlot = slot<List<LaunchEntity>>()
        every {
            db.launchesDao().deleteExistingAndInsertAll(capture(insertEntitiesListSlot))
        } just Runs

        runTest {
            val launchStatusCriteria = FilteringOption.ByLaunchStatus(LaunchStatus.Failed)
            val subject = launchesRepository.getAllLaunchesWithCriteria(
                sortingOption = sortingOption, launchStatusCriteria
            )

            // Verify 1 entry is returned after filtering
            assertEquals(1, subject.size)
            with(expectThat(subject)[0].isA<Launch>()) {
                get(Launch::launchStatus).isEqualTo(LaunchStatus.Failed)
                get(Launch::launchDateTime).isA<DateTime>().get(DateTime::unixTimestamp)
                    .isEqualTo(1482304692)
            }
            // Validate appropriate calls
            coVerify(exactly = 1) {
                service.fetchLaunchesWithRockets(any())
                db.launchesDao().deleteExistingAndInsertAll(any())
            }
            // Validate that all (unfiltered) entities were added to DB
            assertEquals(4, insertEntitiesListSlot.captured.size)
        }
    }

    @Test
    fun `getAllLaunchesWithCriteria should swallow UnknownHostException exception when service throws and return data from db`() {
        val errMsg = "fake error"
        coEvery { service.fetchLaunchesWithRockets(any()) } throws UnknownHostException(errMsg)
        val launchEntity: LaunchEntity = FakeSuccessfulLaunchEntity2015
        val dbLaunchEntities = listOf(launchEntity)
        every {
            db.launchesDao().getAllWithMatchingCriteria(any(), any(), any())
        } returns dbLaunchEntities

        runTest {
            val subject = launchesRepository.getAllLaunchesWithCriteria(
                sortingOption = sortingOption, null,
            )

            coVerify(exactly = 1) {
                service.fetchLaunchesWithRockets(any())
                db.launchesDao().getAllWithMatchingCriteria(any(), any(), any())
            }
            with(expectThat(subject)) {
                get(List<Launch>::size).isEqualTo(1)
                // Verify returned data matches fake DB item
                with(this[0]) {
                    get(Launch::id).isEqualTo(launchEntity.uid)
                    get(Launch::name).isEqualTo(launchEntity.name)
                    get(Launch::launchDateTime).isA<DateTime>().get(DateTime::unixTimestamp)
                        .isEqualTo(launchEntity.dateUnix)
                    with(get(Launch::rocket).isA<Rocket>()) {
                        get(Rocket::name).isEqualTo(launchEntity.rocket!!.name)
                        get(Rocket::type).isEqualTo(launchEntity.rocket!!.type)
                    }
                    with(get(Launch::links).isA<Links>()) {
                        get(Links::articleURL).isEqualTo(launchEntity.links!!.articleURL)
                        get(Links::webcastURL).isEqualTo(launchEntity.links!!.youtubeURL)
                        get(Links::wikiURL).isEqualTo(launchEntity.links!!.wikiURL)
                    }
                    with(get(Launch::launchStatus).isA<LaunchStatus>()) { isEqualTo(LaunchStatus.Successful) }
                }
            }
        }
    }

}