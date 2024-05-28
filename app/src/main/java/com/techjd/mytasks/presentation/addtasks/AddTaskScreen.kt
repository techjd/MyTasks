package com.techjd.mytasks.presentation.addtasks

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.dropUnlessResumed
import com.techjd.mytasks.AddTask
import com.techjd.mytasks.presentation.components.MyTopAppBar
import com.techjd.mytasks.util.Utils

@Composable
fun AddTaskScreen(
  onNavigateBack: (shouldRefresh: Boolean) -> Unit,
  dateInfo: AddTask,
  taskViewModel: AddTaskViewModel = hiltViewModel()
) {
  val context = LocalContext.current

  LaunchedEffect(key1 = taskViewModel.addTaskScreenState.value.isSuccess) {
    with(taskViewModel.addTaskScreenState.value) {
      if (isSuccess) {
        Toast.makeText(context, "Task Added Successfully", Toast.LENGTH_SHORT).show()
        onNavigateBack(true)
      } else {
        if (!errorMessage.isNullOrBlank()) {
          Toast.makeText(
            context, taskViewModel.addTaskScreenState.value.errorMessage, Toast.LENGTH_SHORT
          ).show()
        }
      }
    }
  }

  Column(modifier = Modifier.fillMaxSize()) {
    MyTopAppBar(
      "Add A New Task",
      leadingIcon = {
        Icon(
          Icons.Default.ArrowBack, contentDescription = "back",
          modifier = Modifier.clickable(enabled = true, onClick = dropUnlessResumed {
            onNavigateBack(false)
          })
        )
      }
    )

    TextField(value = taskViewModel.title.value, onValueChange = {
      taskViewModel.title.value = it
    }, placeholder = {
      Text(text = "Enter Title")
    }, modifier = Modifier
      .fillMaxWidth()
      .padding(12.dp),
      leadingIcon = {
        Icon(Icons.Default.Info, contentDescription = "Enter Title")
      }
    )

    TextField(value = taskViewModel.desc.value, onValueChange = {
      taskViewModel.desc.value = it
    }, placeholder = {
      Text(text = "Enter description")
    }, modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      leadingIcon = {
        Icon(Icons.Default.Info, contentDescription = "Enter Title")
      }
    )

    Row(
      modifier = Modifier
        .padding(12.dp)
        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
        .height(56.dp)
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.surfaceVariant),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Icon(
        Icons.Default.DateRange, contentDescription = "Selected Date",
        modifier = Modifier.padding(horizontal = 12.dp)
      )

      Text(text = "${dateInfo.day} ${Utils.getMonthName(dateInfo.month)}, ${dateInfo.year}")
    }

    if (taskViewModel.addTaskScreenState.value.isHittingApi) {
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        CircularProgressIndicator()
      }
    } else {
      Button(
        onClick = {
          taskViewModel.addTask(dateInfo)
        }, modifier = Modifier
        .padding(horizontal = 24.dp, vertical = 12.dp)
        .fillMaxWidth(),
        enabled = taskViewModel.isEnabled
      ) {
        Text(text = "Save", fontWeight = FontWeight.Bold, fontSize = 18.sp)
      }
    }
  }
}