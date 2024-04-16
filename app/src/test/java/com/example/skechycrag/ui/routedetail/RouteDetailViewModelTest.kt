package com.example.skechycrag.ui.routedetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.skechycrag.domain.AddAlertUseCase
import com.example.skechycrag.domain.GetMoreInfoRouteUseCase
import com.example.skechycrag.domain.GetRouteDetailUseCase
import com.example.skechycrag.ui.model.RouteModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.Assert.assertEquals

@ExperimentalCoroutinesApi
class RouteDetailViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()



    private lateinit var viewModel: RouteDetailViewModel
    private val getRouteDetailUseCase: GetRouteDetailUseCase = mockk(relaxed = true)
    private val getMoreInfoRouteUseCase: GetMoreInfoRouteUseCase = mockk(relaxed = true)
    private val addAlertUseCase: AddAlertUseCase = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = RouteDetailViewModel(getRouteDetailUseCase, getMoreInfoRouteUseCase, addAlertUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher to the original dispatcher
    }

    @Test
    fun `searchByName success updates state correctly`() = runTest {
        val cragName = "Guayente"
        val mockData = listOf(
            RouteModel(crag_name = cragName, route_name = "Por fin lunes", grade = "6c+", type = "Sport Climbing", community_grade = ""),
            RouteModel(crag_name = cragName, route_name = "Los mercenarios del grado", grade = "7a+", type = "Sport Climbing", community_grade = "")
        )
        val uiMockData = mockData.map { dataRouteModel ->
            // Convert data layer CragModel to UI layer CragModel here
            com.example.skechycrag.data.model.route.RouteModel(
                crag_name = dataRouteModel.crag_name,
                route_name = dataRouteModel.route_name,
                grade = dataRouteModel.grade,
                type = dataRouteModel.type,
                community_grade = dataRouteModel.community_grade
            )
        }
        coEvery { getRouteDetailUseCase(cragName) } returns uiMockData

        val results = mutableListOf<RouteDetailState>()
        val job = launch { viewModel.routeDetailState.toList(results) }

        viewModel.searchByName(cragName)

        advanceUntilIdle() // Ensure all coroutines are completed

        //assertTrue(results[0] is RouteDetailState.Loading, "First state should be Loading")
        assertEquals("Second state should be Success", RouteDetailState.Success(mockData), results[0])

        job.cancel()
    }

    @Test
    fun `searchByName error updates state correctly`() = runTest {
        val cragName = "Nowhere Land"
        coEvery { getRouteDetailUseCase(cragName) } returns emptyList()

        val results = mutableListOf<RouteDetailState>()
        val job = launch { viewModel.routeDetailState.toList(results) }

        viewModel.searchByName(cragName)

        advanceUntilIdle() // Wait until all coroutines are idle before proceeding with the assertions

        assertTrue(results[0] is RouteDetailState.Error, "Second state should be Error")

        job.cancel()
    }
    @Test
    fun `addAlert calls use case`() = runTest {
        val alertMessage = "New Alert"
        val routeName = "Highball Heaven"

        coEvery { addAlertUseCase(alertMessage, routeName) } returns Unit

        // Directly call the suspending function since runTest provides a coroutine scope
        viewModel.addAlert(alertMessage, routeName)

        advanceUntilIdle() // Ensure coroutine has completed

        // Verify that the use case was called with the correct parameters
        coVerify { addAlertUseCase(alertMessage, routeName) }
    }

}
