package com.joaquincollazoruiz.spacex.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.joaquincollazoruiz.spacex.domain.model.FilteringOption
import com.joaquincollazoruiz.spacex.domain.model.Launch
import com.joaquincollazoruiz.spacex.domain.model.LaunchStatus
import com.joaquincollazoruiz.spacex.domain.model.SortingOption
import com.joaquincollazoruiz.spacex.domain.usecase.IGetLaunchesUseCase
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

@ExperimentalCoroutinesApi
class LaunchesViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var getLaunchesUseCase: IGetLaunchesUseCase

    // Subject
    private lateinit var launchesViewModel: LaunchesViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
        // Set up [launchesViewModel] subject in individual cases after mocking since it auto-starts fetching.
        // Use Dispatchers.Main which points to test setup
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `view model should automatically get launch on init`() =
        runTest {
            // Mock
            val launches: List<Launch> = listOf(mockk())
            coEvery { getLaunchesUseCase.execute() } returns IGetLaunchesUseCase.Result.Success(
                launches
            )

            // Subject
            launchesViewModel = LaunchesViewModel(
                getLaunchesUseCase = getLaunchesUseCase,
                defaultDispatcher = Dispatchers.Main
            )

            // Validate
            advanceUntilIdle()
            coVerify(exactly = 1) { getLaunchesUseCase.execute() }
        }

    @Test
    fun `selected sorting option should be delegated to use case and livedata`() =
        runTest {
            // Mock error state initially
            val sortingOptionSlot = slot<SortingOption>()
            coEvery { getLaunchesUseCase.execute(sortingOption = capture(sortingOptionSlot)) } returns IGetLaunchesUseCase.Result.Error(
                Exception("fake")
            )

            // Subject
            launchesViewModel = LaunchesViewModel(
                getLaunchesUseCase = getLaunchesUseCase,
                defaultDispatcher = Dispatchers.Main
            )
            // Select sort option
            launchesViewModel.handleUserAction(
                LaunchesViewModel.UserAction.SelectSortingPreference(
                    SortingOption.Ascending
                )
            )

            // Validate
            advanceUntilIdle()
            val actualUseCaseFilteringOption = sortingOptionSlot.captured
            expectThat(actualUseCaseFilteringOption).isEqualTo(SortingOption.Ascending)
            expectThat(launchesViewModel.sortingPreference.value).isEqualTo(SortingOption.Ascending)
        }

    @Test
    fun `selected launch status filtering option should be delegated to use case and livedata`() =
        runTest {
            // Mock error state initially
            val filterOptionSlot = slot<FilteringOption.ByLaunchStatus>()
            coEvery { getLaunchesUseCase.execute(filterStatusOption = capture(filterOptionSlot)) } returns IGetLaunchesUseCase.Result.Error(
                Exception("fake")
            )

            // Subject
            launchesViewModel = LaunchesViewModel(
                getLaunchesUseCase = getLaunchesUseCase,
                defaultDispatcher = Dispatchers.Main
            )
            // Select filter option
            launchesViewModel.handleUserAction(
                LaunchesViewModel.UserAction.SelectStatusFilteringPreference(
                    FilteringOption.ByLaunchStatus(LaunchStatus.Failed)
                )
            )

            // Validate
            advanceUntilIdle()
            val actualUseCaseFilteringOption = filterOptionSlot.captured
            expectThat(actualUseCaseFilteringOption).isEqualTo(
                FilteringOption.ByLaunchStatus(
                    LaunchStatus.Failed
                )
            )
            expectThat(launchesViewModel.filterStatusPreference.value).isEqualTo(
                FilteringOption.ByLaunchStatus(LaunchStatus.Failed)
            )
        }

    @Test
    fun `selected year filtering option should be delegated to use case and livedata`() =
        runTest {
            // Mock error state initially
            val filterOptionSlot = slot<FilteringOption.ByYear>()
            coEvery { getLaunchesUseCase.execute(filterYearOption = capture(filterOptionSlot)) } returns IGetLaunchesUseCase.Result.Error(
                Exception("fake")
            )

            // Subject
            launchesViewModel = LaunchesViewModel(
                getLaunchesUseCase = getLaunchesUseCase,
                defaultDispatcher = Dispatchers.Main
            )
            // Select filter option
            launchesViewModel.handleUserAction(
                LaunchesViewModel.UserAction.SelectYearFilteringPreference(
                    FilteringOption.ByYear(2010)
                )
            )

            // Validate
            advanceUntilIdle()
            val actualUseCaseFilteringOption = filterOptionSlot.captured
            expectThat(actualUseCaseFilteringOption).isEqualTo(FilteringOption.ByYear(2010))
            expectThat(launchesViewModel.filterYearPreference.value).isEqualTo(
                FilteringOption.ByYear(
                    2010
                )
            )
        }
}