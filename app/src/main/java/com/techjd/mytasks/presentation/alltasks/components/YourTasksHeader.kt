package com.techjd.mytasks.presentation.alltasks.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techjd.mytasks.domain.model.calendar.DateInfo
import com.techjd.mytasks.util.Utils

@Composable
fun YourTasksHeader(
  dateInfo: DateInfo,
  onAddTask: () -> Unit,
) {
  Row(
    modifier = Modifier
      .background(MaterialTheme.colorScheme.background)
      .fillMaxWidth()
      .padding(horizontal = 16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column {
      Text(text = "Your Tasks ✅", fontWeight = FontWeight.Bold, fontSize = 18.sp)
      Text(text = Utils.getFormattedDate(dateInfo), fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
    }
    Spacer(modifier = Modifier.weight(1f))
    Button(onClick = { onAddTask() }) {
      Text(text = "Add Task")
    }
  }
}
