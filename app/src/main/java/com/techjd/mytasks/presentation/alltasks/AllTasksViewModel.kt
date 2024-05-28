package com.techjd.mytasks.presentation.alltasks

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techjd.mytasks.data.NetworkResult.Error
import com.techjd.mytasks.data.NetworkResult.Exception
import com.techjd.mytasks.data.NetworkResult.Success
import com.techjd.mytasks.data.remote.dto.response.tasks.toTasks
import com.techjd.mytasks.domain.model.tasks.Task
import com.techjd.mytasks.domain.model.tasks.Tasks
import com.techjd.mytasks.domain.usecase.DeleteTaskUseCase
import com.techjd.mytasks.domain.usecase.GetAllTasksUseCase
import com.techjd.mytasks.domain.model.calendar.DateInfo
import com.techjd.mytasks.util.Utils
import com.techjd.mytasks.util.next
import com.techjd.mytasks.util.previous
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class AllTasksViewModel @Inject constructor(
  private val getAllTasksUseCase: GetAllTasksUseCase,
  private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {
  val currentMonth = mutableStateOf(Utils.getCurrentMonthAndYear())
  val dates = mutableStateOf(Utils.getAllDaysInMonth(Utils.getCurrentMonthAndYear()))
  val selectedDate = mutableStateOf(Utils.getTodayDate())

  val screenState = mutableStateOf(AllTasksScreenState())
  var datesMap = mutableMapOf<Long, Int>()

  private val _sharedFlow: MutableSharedFlow<DeleteEvents> = MutableSharedFlow()
  val shareFlow: SharedFlow<DeleteEvents> = _sharedFlow

  init {
    getAllTasks()
  }

  fun next() {
    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        currentMonth.value = currentMonth.value.next()
        dates.value = Utils.getAllDaysInMonth(currentMonth.value)
        addTotalTasks()
      }
    }
  }

  fun previous() {
    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        currentMonth.value = currentMonth.value.previous()
        dates.value = Utils.getAllDaysInMonth(currentMonth.value)
        addTotalTasks()
      }
    }
  }

  fun changeSelectedDate(date: DateInfo) {
    selectedDate.value = date
    filterTasks()
  }

  fun getAllTasks() {
    viewModelScope.launch {
      screenState.value = screenState.value.copy(isLoading = true)
      when (val data = getAllTasksUseCase.invoke()) {
        is Error -> {
          screenState.value = screenState.value.copy(
            isLoading = false, error = data.message ?: "Please Try Again Later"
          )
        }

        is Exception -> {
          screenState.value = screenState.value.copy(
            isLoading = false, error = data.e.message ?: "Please Try Again Later"
          )
        }

        is Success -> {
          screenState.value = screenState.value.copy(tasks = data.data.toTasks(), error = null)
          filterTasks()
          fillHashMap()
        }
      }
    }
  }

  private fun fillHashMap() {
    viewModelScope.launch(Dispatchers.Default) {
      datesMap.clear()
      screenState.value.tasks.tasks.forEach {
        it.taskDetail.date?.let { epochDate ->
          datesMap[epochDate] = datesMap.getOrDefault(epochDate, 0) + 1
        }
      }
      addTotalTasks()
    }
  }

  private fun addTotalTasks() {
    viewModelScope.launch {
      withContext(Dispatchers.Default) {
        dates.value = dates.value.map { dateInfo ->
          val epochDate = Utils.dateToEpoch(dateInfo.day, dateInfo.month, dateInfo.year)
          if (datesMap.containsKey(epochDate)) {
            DateInfo(
              day = dateInfo.day,
              month = dateInfo.month,
              year = dateInfo.year,
              dayOfWeek = dateInfo.dayOfWeek,
              numberOfTasks = datesMap[epochDate] ?: 0
            )
          } else {
            DateInfo(
              day = dateInfo.day,
              month = dateInfo.month,
              year = dateInfo.year,
              dayOfWeek = dateInfo.dayOfWeek,
              numberOfTasks = 0
            )
          }
        }

        screenState.value =
          screenState.value.copy(isLoading = false)
      }
    }
  }

  private fun filterTasks() {
    viewModelScope.launch(Dispatchers.Default) {
      val filteredTask = mutableListOf<Task>()
      screenState.value.tasks.tasks.forEach {
        if (it.taskDetail.date?.equals(
            Utils.dateToEpoch(
              selectedDate.value.day, selectedDate.value.month, selectedDate.value.year
            )
          ) == true
        ) {
          filteredTask.add(it)
        }
      }
      screenState.value =
        screenState.value.copy(filteredTasks = Tasks(filteredTask))
    }
  }

  fun deleteTask(taskId: Long) {
    viewModelScope.launch(Dispatchers.IO) {
      screenState.value = screenState.value.copy(isHittingDeleteApi = true)
      when (val data = deleteTaskUseCase.invoke(taskId)) {
        is Error -> {
          screenState.value = screenState.value.copy(isHittingDeleteApi = false)
          _sharedFlow.emit(DeleteEvents.Error(data.message ?: "Please Try Again Later"))
        }

        is Exception -> {
          screenState.value = screenState.value.copy(isHittingDeleteApi = false)
          _sharedFlow.emit(DeleteEvents.Error(data.e.message ?: "Please Try Again Later"))
        }

        is Success -> {
          screenState.value = screenState.value.copy(isHittingDeleteApi = false)
          _sharedFlow.emit(DeleteEvents.Success)
          removeTask(taskId = taskId)
        }
      }
    }
  }

  private fun removeTask(taskId: Long) {
    viewModelScope.launch(Dispatchers.Default) {
      val filteredTasks = (screenState.value.filteredTasks.tasks as MutableList<Task>).filter {
        it.taskId != taskId
      }
      val originalList = (screenState.value.tasks.tasks as MutableList<Task>).filter {
        it.taskId != taskId
      }
      screenState.value = screenState.value.copy(
        filteredTasks = Tasks(filteredTasks),
        tasks = Tasks(originalList)
      )
      fillHashMap()
    }
  }
}