package com.techjd.mytasks.presentation.alltasks.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.techjd.mytasks.domain.model.calendar.DateInfo

@Composable
fun CircularText(
  dateInfo: DateInfo,
  isSelected: Boolean,
  onItemClick: () -> Unit
) {
  Column(
    modifier = Modifier
      .clickable {
        onItemClick()
      }
      .clip(RoundedCornerShape(4.dp))
      .size(40.dp)
      .background(
        if (isSelected) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.secondaryContainer
      ),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = dateInfo.day.toString(),
      style = MaterialTheme.typography.bodyMedium,
      color = if (isSelected) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onSecondaryContainer,
      fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
    )
    if (dateInfo.numberOfTasks != 0) {
      Text(
        text = dateInfo.numberOfTasks.toString(),
        style = MaterialTheme.typography.bodyMedium,
        color = if (isSelected) MaterialTheme.colorScheme.onTertiaryContainer else MaterialTheme.colorScheme.onSecondaryContainer,
        fontWeight = FontWeight.Bold,
      )
    }
  }
}