package com.techjd.mytasks.presentation.addtasks

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techjd.mytasks.AddTask
import com.techjd.mytasks.data.NetworkResult.Error
import com.techjd.mytasks.data.NetworkResult.Exception
import com.techjd.mytasks.data.NetworkResult.Success
import com.techjd.mytasks.domain.usecase.AddNewTaskUseCase
import com.techjd.mytasks.util.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AddTaskViewModel @Inject constructor(
  private val addNewTaskUseCase: AddNewTaskUseCase,
) : ViewModel() {
  val title = mutableStateOf("")
  val desc = mutableStateOf("")

  val isEnabled by
  derivedStateOf {
    title.value.isNotEmpty()
  }

  val addTaskScreenState = mutableStateOf(AddTaskScreenState())

  fun addTask(
    addTask: AddTask
  ) {
    viewModelScope.launch {
      addTaskScreenState.value = addTaskScreenState.value.copy(
        isHittingApi = true,
        isSuccess = false
      )
      println("Called ${title.value} ${desc.value} ${Utils.dateToEpoch(addTask.day, addTask.month, addTask.year)}")
      val data = addNewTaskUseCase(
        title = title.value,
        date = Utils.dateToEpoch(addTask.day, addTask.month, addTask.year),
        description = desc.value
      )
      println("I added ${data}")
      when (data) {
        is Error -> {
          addTaskScreenState.value = addTaskScreenState.value.copy(
            isHittingApi = false,
            isSuccess = false,
            errorMessage = data.message ?: "Please Try Again Later"
          )
        }

        is Exception -> {
          addTaskScreenState.value = addTaskScreenState.value.copy(
            isHittingApi = false,
            isSuccess = false,
            errorMessage = data.e.message ?: "Please Try Again Later"
          )
        }

        is Success -> {
          addTaskScreenState.value = addTaskScreenState.value.copy(
            isHittingApi = false,
            isSuccess = true,
            errorMessage = null
          )
        }
      }
    }
  }
}