package com.techjd.mytasks.domain.model.calendar

data class DateInfo(
  val year: Int, val month: Int, val day: Int, val dayOfWeek: String, val numberOfTasks: Int = 0
)

