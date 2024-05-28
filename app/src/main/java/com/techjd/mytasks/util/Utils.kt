package com.techjd.mytasks.util

import com.techjd.mytasks.domain.model.calendar.DateInfo
import com.techjd.mytasks.domain.model.calendar.MonthYear
import java.util.Calendar
import java.util.TimeZone

object Utils {
  fun getAllDaysInMonth(monthYear: MonthYear): List<DateInfo> {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, monthYear.year)
    calendar.set(Calendar.MONTH, monthYear.month - 1) // Calendar months are 0-based
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val days = mutableListOf<DateInfo>()

    for (day in 1..daysInMonth) {
      calendar.set(Calendar.DAY_OF_MONTH, day)
      val dayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))
      val dateInfo = DateInfo(
        year = calendar.get(Calendar.YEAR),
        month = calendar.get(Calendar.MONTH) + 1,
        day = calendar.get(Calendar.DAY_OF_MONTH),
        dayOfWeek = dayOfWeek
      )
      days.add(dateInfo)
    }

    return days
  }

  fun getDayOfWeek(day: Int): String {
    return when (day) {
      Calendar.SUNDAY -> "SUN"
      Calendar.MONDAY -> "MON"
      Calendar.TUESDAY -> "TUE"
      Calendar.WEDNESDAY -> "WED"
      Calendar.THURSDAY -> "THU"
      Calendar.FRIDAY -> "FRI"
      Calendar.SATURDAY -> "SAT"
      else -> "UNK"
    }
  }

  fun getIndex(weekDay: String): Int {
    return when (weekDay) {
      "SUN" -> 0
      "MON" -> 1
      "TUE" -> 2
      "WED" -> 3
      "THU" -> 4
      "FRI" -> 5
      "SAT" -> 6
      else -> 0
    }
  }

  fun getCurrentMonthAndYear(): MonthYear {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1 // Calendar months are 0-based
    return MonthYear(month, year)
  }

  fun getTodayDate(): DateInfo {
    val calendar = Calendar.getInstance()
    return DateInfo(
      year = calendar.get(Calendar.YEAR),
      month = calendar.get(Calendar.MONTH) + 1,
      day = calendar.get(Calendar.DAY_OF_MONTH),
      dayOfWeek = getDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK))
    )
  }

  fun getMonthName(month: Int): String {
    return when (month) {
      1 -> "January"
      2 -> "February"
      3 -> "March"
      4 -> "April"
      5 -> "May"
      6 -> "June"
      7 -> "July"
      8 -> "August"
      9 -> "September"
      10 -> "October"
      11 -> "November"
      12 -> "December"
      else -> "Unknown"
    }
  }

  fun dateToEpoch(day: Int, month: Int, year: Int): Long {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendar.set(year, month, day, 0, 0, 0)  // Month is 0-based in Calendar but we have already done minus from month
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis / 1000  // Convert milliseconds to seconds
  }

  fun epochToDate(epoch: Long): Calendar {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendar.timeInMillis = epoch * 1000  // Convert seconds to milliseconds
    return calendar
  }

  fun getFormattedDate(dateInfo: DateInfo): String {
    return "${dateInfo.day} ${getMonthName(dateInfo.month)} ,${dateInfo.year}"
  }

  fun previousMonth(value: MonthYear): MonthYear {
    return value.previous()
  }

  fun nextMonth(value: MonthYear): MonthYear {
    return value.next()
  }
}