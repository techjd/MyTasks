package com.techjd.mytasks.presentation.alltasks

sealed class DeleteEvents(val message: String? = null) {
  data object Success : DeleteEvents()
  class Error(message: String) : DeleteEvents(message)
}