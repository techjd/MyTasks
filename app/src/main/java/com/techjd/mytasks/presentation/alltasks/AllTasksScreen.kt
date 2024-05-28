package com.techjd.mytasks.presentation.alltasks

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.techjd.mytasks.domain.model.tasks.Task
import com.techjd.mytasks.domain.model.calendar.DateInfo
import com.techjd.mytasks.presentation.alltasks.DeleteEvents.Error
import com.techjd.mytasks.presentation.alltasks.DeleteEvents.Success
import com.techjd.mytasks.presentation.alltasks.components.AddTaskButton
import com.techjd.mytasks.presentation.alltasks.components.MyAlertDialog
import com.techjd.mytasks.presentation.alltasks.components.MyCalendar
import com.techjd.mytasks.presentation.alltasks.components.TaskItem
import com.techjd.mytasks.presentation.alltasks.components.YourTasksHeader
import com.techjd.mytasks.presentation.components.MyTopAppBar
import com.techjd.mytasks.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
  allTasksViewModel: AllTasksViewModel = hiltViewModel(),
  modifier: Modifier = Modifier,
  onAddTask: (dateInfo: DateInfo) -> Unit,
  shouldLoad: Boolean
) {
  val context = LocalContext.current
  val lifeCycleOwner = LocalLifecycleOwner.current

  var openAlertDialog by rememberSaveable { mutableStateOf(false) }

  var selectedTaskToDelete by rememberSaveable {
    mutableStateOf<Task?>(null)
  }

  LaunchedEffect(shouldLoad) {
    if (shouldLoad) allTasksViewModel.getAllTasks()
  }

  LaunchedEffect(key1 = Unit) {
    lifeCycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
      withContext(Dispatchers.Main.immediate) {
        allTasksViewModel.shareFlow.collect {
          when (it) {
            is Error -> {
              openAlertDialog = false
              selectedTaskToDelete = null
              Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }

            Success -> {
              Log.d("AllTasksScreen", "HomeScreen: Close Dialog")
              openAlertDialog = false
              selectedTaskToDelete = null
              Toast.makeText(context, "Task Removed SuccessFully", Toast.LENGTH_SHORT).show()
            }
          }
        }
      }
    }
  }

  Column(modifier = modifier.fillMaxSize()) {
    MyTopAppBar(title = "My Tasks \uD83D\uDCCB")
    with(allTasksViewModel.screenState.value) {
      if (isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
          CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
      } else if (!error.isNullOrBlank()) {
        Column(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(text = error, textAlign = TextAlign.Center)
          Spacer(modifier = Modifier.height(2.dp))
          Button(onClick = { allTasksViewModel.getAllTasks() }) {
            Text(text = "Retry")
          }
        }
      } else {
        MyCalendar(
          onNext = {
            allTasksViewModel.next()
          },
          onPrevious = {
            allTasksViewModel.previous()
          },
          selectedDate = allTasksViewModel.selectedDate.value,
          dates = allTasksViewModel.dates.value,
          onDateSelected = { selectedDate ->
            allTasksViewModel.changeSelectedDate(selectedDate)
          },
          currentMonth = allTasksViewModel.currentMonth.value
        )

        if (allTasksViewModel.screenState.value.isLoading) {
          Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
          }
        } else {
          LazyColumn(
            modifier = Modifier
              .padding(vertical = 12.dp)
              .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            with(allTasksViewModel.screenState.value.filteredTasks) {
              if (this.tasks.isEmpty()) {
                item {
                  Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                  ) {
                    Text(
                      text = "No Tasks Added for ${allTasksViewModel.selectedDate.value.day} ${
                        Utils.getMonthName(
                          allTasksViewModel.selectedDate.value.month
                        )
                      } , ${allTasksViewModel.selectedDate.value.year}",
                      textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    AddTaskButton {
                      onAddTask(allTasksViewModel.selectedDate.value)
                    }
                  }
                }
              } else {
                stickyHeader {
                  YourTasksHeader(
                    dateInfo = allTasksViewModel.selectedDate.value
                  ) {
                    onAddTask(allTasksViewModel.selectedDate.value)
                  }
                }

                items(this.tasks) { task ->
                  TaskItem(task = task) {
                    selectedTaskToDelete = task
                    openAlertDialog = true
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  if (openAlertDialog) {
    MyAlertDialog(
      onDismissRequest = {
        if (!allTasksViewModel.screenState.value.isHittingDeleteApi) {
          openAlertDialog = false
        } else {
          openAlertDialog = true
        }
      }, onConfirmation = {
      selectedTaskToDelete?.let {
        allTasksViewModel.deleteTask(it.taskId)
      }
    }, dialogTitle = "Do you want to delete this Task ?",
      icon = Outlined.Delete,
      isLoading = allTasksViewModel.screenState.value.isHittingDeleteApi
    )
  }
}

internal fun areDateInfoSame(
  dateInfo1: DateInfo,
  dateInfo2: DateInfo
): Boolean {
  return dateInfo1.day == dateInfo2.day && dateInfo1.month == dateInfo2.month && dateInfo1.year == dateInfo2.year
}