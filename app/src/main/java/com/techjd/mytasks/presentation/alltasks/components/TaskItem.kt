package com.techjd.mytasks.presentation.alltasks.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techjd.mytasks.domain.model.tasks.Task

@Composable
fun TaskItem(
  task: Task,
  onClick: () -> Unit
) {
  Column(
    verticalArrangement = Arrangement.Center,
    modifier = Modifier
      .padding(horizontal = 8.dp)
      .clickable {
        onClick()
      }
      .clip(RoundedCornerShape(8.dp))
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.surfaceVariant)
      .padding(8.dp)
  ) {
    Text(text = task.taskDetail.title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
    Spacer(modifier = Modifier.height(4.dp))
    task.taskDetail.description?.let {
      if (it.isNotBlank()) {
        Text(text = it)
      }
    }
  }
}
