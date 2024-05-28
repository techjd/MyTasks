package com.techjd.mytasks.presentation.alltasks

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells.Fixed
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
import com.techjd.mytasks.presentation.alltasks.SwipeDirection.LEFT
import com.techjd.mytasks.presentation.alltasks.SwipeDirection.RIGHT
import com.techjd.mytasks.presentation.alltasks.components.AddTaskButton
import com.techjd.mytasks.presentation.alltasks.components.CircularText
import com.techjd.mytasks.presentation.alltasks.components.MyAlertDialog
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

  var offsetX by rememberSaveable { mutableStateOf(0f) }
  var swipeDirection by rememberSaveable { mutableStateOf(LEFT) }
  var shouldShowSheet by rememberSaveable {
    mutableStateOf(true)
  }

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
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
          IconButton(onClick = {
            allTasksViewModel.previous()
          }) {
            Icon(Filled.ArrowBack, contentDescription = "Previous")
          }
          Row(
            modifier = Modifier
              .clickable {
                shouldShowSheet = !shouldShowSheet
              }
              .weight(1f),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "${
                Utils.getMonthName(
                  allTasksViewModel.currentMonth.value.month
                )
              } , ${allTasksViewModel.currentMonth.value.year}",
              textAlign = TextAlign.Left,
              style = MaterialTheme.typography.bodyLarge,
              fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
              if (shouldShowSheet) Filled.KeyboardArrowUp else Filled.KeyboardArrowDown,
              contentDescription = "Open"
            )
          }
          IconButton(onClick = {
            allTasksViewModel.next()
          }) {
            Icon(Filled.ArrowForward, contentDescription = "Next")
          }
        }

        AnimatedVisibility(visible = shouldShowSheet) {
          LazyVerticalGrid(
            columns = Fixed(7),
            contentPadding = PaddingValues(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.pointerInput(Unit) {
              detectHorizontalDragGestures(
                onDragEnd = {
                  if (swipeDirection == LEFT) {
                    allTasksViewModel.next()
                  } else {
                    allTasksViewModel.previous()
                  }
                }
              ) { change, dragAmount ->
                offsetX += dragAmount
                swipeDirection = if (dragAmount > 0) {
                  RIGHT
                } else {
                  LEFT
                }
                change.consume()
              }
            }
          ) {
            items(listOf("S", "M", "T", "W", "T", "F", "S")) {
              Box(
                modifier = Modifier
                  .size(30.dp)
                  .clip(CircleShape), contentAlignment = Alignment.Center
              ) {
                Text(
                  text = it, textAlign = TextAlign.Center,
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.SemiBold
                )
              }
            }
            if (allTasksViewModel.dates.value.isNotEmpty()) {
              val firstDayIndex = Utils.getIndex(allTasksViewModel.dates.value.first().dayOfWeek)
              items(firstDayIndex) {
                Text(text = "")
              }
            }

            itemsIndexed(allTasksViewModel.dates.value) { index, item ->
              CircularText(
                dateInfo = item, areDateInfoSame(allTasksViewModel.selectedDate.value, item)
              ) {
                allTasksViewModel.changeSelectedDate(item)
              }
            }
          }
        }

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

internal enum class SwipeDirection {
  RIGHT,
  LEFT
}