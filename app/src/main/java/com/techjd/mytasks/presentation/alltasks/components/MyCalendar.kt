package com.techjd.mytasks.presentation.alltasks.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells.Fixed
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.techjd.mytasks.domain.model.calendar.DateInfo
import com.techjd.mytasks.domain.model.calendar.MonthYear
import com.techjd.mytasks.presentation.alltasks.areDateInfoSame
import com.techjd.mytasks.presentation.alltasks.components.SwipeDirection.LEFT
import com.techjd.mytasks.presentation.alltasks.components.SwipeDirection.RIGHT
import com.techjd.mytasks.util.Utils

@Composable
fun MyCalendar(
  onNext: () -> Unit,
  onPrevious: () -> Unit,
  onDateSelected: (DateInfo) -> Unit,
  selectedDate: DateInfo,
  dates: List<DateInfo>,
  currentMonth: MonthYear
) {
  var offsetX by rememberSaveable { mutableStateOf(0f) }
  var swipeDirection by rememberSaveable { mutableStateOf(LEFT) }
  var shouldShowSheet by rememberSaveable {
    mutableStateOf(true)
  }

  Column {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
      IconButton(onClick = {
        onPrevious()
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
              currentMonth.month
            )
          } , ${currentMonth.year}",
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
        onNext()
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
                onNext()
              } else {
                onPrevious()
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
        if (dates.isNotEmpty()) {
          val firstDayIndex = Utils.getIndex(dates.first().dayOfWeek)
          items(firstDayIndex) {
            Text(text = "")
          }
        }

        itemsIndexed(dates) { index, item ->
          CircularText(
            dateInfo = item, areDateInfoSame(selectedDate, item)
          ) {
            onDateSelected(item)
          }
        }
      }
    }
  }
}

internal enum class SwipeDirection {
  RIGHT,
  LEFT
}