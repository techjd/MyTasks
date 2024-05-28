package com.techjd.mytasks

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.techjd.mytasks.presentation.addtasks.AddTaskScreen
import com.techjd.mytasks.presentation.alltasks.HomeScreen
import com.techjd.mytasks.util.Constants

@Composable
fun TasksAppEntryScreenApp() {
  val navController = rememberNavController()
  Scaffold(
    content = { innerPadding ->
      NavHost(
        startDestination = AllTasks, navController = navController,
        modifier = Modifier.padding(innerPadding)
      ) {
        composable<AllTasks> {
          val shouldRefresh = it.savedStateHandle.get<Boolean>(Constants.SHOULD_LOAD_KEY) ?: false
          HomeScreen(onAddTask = { dateInfo ->
            navController.navigate(
              AddTask(
                dateInfo.day, dateInfo.month,
                dateInfo.year
              )
            )
          }, shouldLoad = shouldRefresh)
        }
        composable<AddTask> {
          val dateInfo: AddTask = it.toRoute()
          AddTaskScreen(onNavigateBack = { shouldLoad ->
            navController.previousBackStackEntry?.savedStateHandle?.set(
              Constants.SHOULD_LOAD_KEY, shouldLoad
            )
            navController.popBackStack()
          }, dateInfo = dateInfo)
        }
      }
    }
  )
}