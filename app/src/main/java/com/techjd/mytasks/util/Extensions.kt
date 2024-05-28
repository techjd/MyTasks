package com.techjd.mytasks.util

import com.techjd.mytasks.domain.model.calendar.MonthYear

fun MonthYear.next(): MonthYear {
  return if (month == 12) {
    MonthYear(1, year + 1)
  } else {
    MonthYear(month + 1, year)
  }
}

fun MonthYear.previous(): MonthYear {
  return if (month == 1) {
    MonthYear(12, year - 1)
  } else {
    MonthYear(month - 1, year)
  }
}