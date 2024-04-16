package com.example.skechycrag.ui.login

import com.example.skechycrag.domain.GetUsersUseCase
import com.example.skechycrag.ui.login.UserDetailState
import com.example.skechycrag.ui.login.UserViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UserViewModelTest {

    @MockK
    lateinit var getUsersUseCase: GetUsersUseCase

    // A test dispatcher for coroutines
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var userViewModel: UserViewModel

    @Before
    fun setUp() {
        // Initialize MockK annotations
        MockKAnnotations.init(this)

        // Apply the test dispatcher
        Dispatchers.setMain(testDispatcher)

        // Initialize the ViewModel with the mocked use case
        userViewModel = UserViewModel(getUsersUseCase)
    }

    @After
    fun tearDown() {
        // Reset the main dispatcher after the test is done
        Dispatchers.resetMain()
    }

    @Test
    fun `check login user`() = runTest {
        // Arrange
        val username = "Edu"
        val password = "3"
        coEvery { getUsersUseCase(username, password) } returns true

        // Act
        userViewModel.checkUser(username, password)

        // Assert
        val value = userViewModel.userState.first()
        assertTrue(value is UserDetailState.Success && value.work)
    }

    @Test
    fun `check wrong user`() = runTest {
        // Arrange
        val username = "wrongUser"
        val password = "wrongPass"
        coEvery { getUsersUseCase(username, password) } returns false

        // Act
        userViewModel.checkUser(username, password)

        // Assert
        val value = userViewModel.userState.first()
        assertTrue(value is UserDetailState.Error && !value.error)
    }


}
