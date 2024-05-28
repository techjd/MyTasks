package com.techjd.mytasks.domain.usecase

import com.techjd.mytasks.domain.repository.ITasksRepository
import javax.inject.Inject

class AddNewTaskUseCase @Inject constructor(
  private val iTasksRepository: ITasksRepository
) {
  suspend operator fun invoke(
    title: String,
    date: Long,
    description: String
  ) = iTasksRepository.addTasks(title, date, description)
}