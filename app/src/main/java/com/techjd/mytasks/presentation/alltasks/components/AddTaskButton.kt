package com.techjd.mytasks.presentation.alltasks.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun AddTaskButton(onAddTask: () -> Unit) {
  Button(onClick = onAddTask) {
    Text(text = "Add Task", fontWeight = FontWeight.Bold)
  }
}
