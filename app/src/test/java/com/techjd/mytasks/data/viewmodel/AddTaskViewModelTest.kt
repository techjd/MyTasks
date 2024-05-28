package com.techjd.mytasks.data.viewmodel

import com.techjd.mytasks.AddTask
import com.techjd.mytasks.data.NetworkResult
import com.techjd.mytasks.data.NetworkResult.Error
import com.techjd.mytasks.data.NetworkResult.Success
import com.techjd.mytasks.data.remote.dto.response.success.SuccessDto
import com.techjd.mytasks.data.remote.dto.response.tasks.TaskDetailDto
import com.techjd.mytasks.data.remote.dto.response.tasks.TaskDto
import com.techjd.mytasks.data.remote.dto.response.tasks.TasksDto
import com.techjd.mytasks.domain.usecase.AddNewTaskUseCase
import com.techjd.mytasks.domain.usecase.DeleteTaskUseCase
import com.techjd.mytasks.domain.usecase.GetAllTasksUseCase
import com.techjd.mytasks.presentation.addtasks.AddTaskViewModel
import com.techjd.mytasks.presentation.alltasks.AllTasksViewModel
import com.techjd.mytasks.util.Utils
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import java.util.Calendar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("All Tasks ViewModel Test")
@OptIn(ExperimentalCoroutinesApi::class)
class AddTaskViewModelTest {
  private val addNewTaskUseCase = mockk<AddNewTaskUseCase>()
  private lateinit var addTaskViewModel: AddTaskViewModel
  private val dispatcher = StandardTestDispatcher()

  @BeforeEach
  fun setUp() {
    Dispatchers.setMain(dispatcher)
  }

  @Test
  @DisplayName("Add Task - success")
  fun addTaskSuccess() = runTest {
    mockkStatic(Utils::class)
    mockkStatic(Calendar::class)

    coEvery {
      addNewTaskUseCase("JD", any(), "")
    } returns Success(
      SuccessDto("Success")
    )

    addTaskViewModel = AddTaskViewModel(addNewTaskUseCase)

    every {
      Utils.dateToEpoch(any(), any(), any())
    } returns 1

    addTaskViewModel.title.value = "JD"
    addTaskViewModel.desc.value = ""

    addTaskViewModel.addTask(AddTask(1,1,1))

    advanceUntilIdle()

    println(addTaskViewModel.addTaskScreenState.value)

    Assertions.assertEquals(false, addTaskViewModel.addTaskScreenState.value.isHittingApi)
    Assertions.assertEquals(true, addTaskViewModel.addTaskScreenState.value.isSuccess)
    Assertions.assertNull(addTaskViewModel.addTaskScreenState.value.errorMessage)
  }

  @Test
  @DisplayName("Add Task - failure")
  fun addTaskFailure() = runTest {
    mockkStatic(Utils::class)
    mockkStatic(Calendar::class)

    coEvery {
      addNewTaskUseCase("JD", any(), "")
    } returns NetworkResult.Error(400, "Failed")

    addTaskViewModel = AddTaskViewModel(addNewTaskUseCase)

    every {
      Utils.dateToEpoch(any(), any(), any())
    } returns 1

    addTaskViewModel.title.value = "JD"
    addTaskViewModel.desc.value = ""

    addTaskViewModel.addTask(AddTask(1,1,1))

    advanceUntilIdle()

    println(addTaskViewModel.addTaskScreenState.value)

    Assertions.assertEquals(false, addTaskViewModel.addTaskScreenState.value.isHittingApi)
    Assertions.assertEquals(false, addTaskViewModel.addTaskScreenState.value.isSuccess)
    Assertions.assertNotNull(addTaskViewModel.addTaskScreenState.value.errorMessage)
  }

  @AfterEach
  fun tearDown() {
    Dispatchers.resetMain()
  }
}