package com.techjd.mytasks.domain.usecase

import com.techjd.mytasks.domain.repository.ITasksRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
  private val iTasksRepository: ITasksRepository
) {
  suspend operator fun invoke(
    taskId: Long
  ) = iTasksRepository.deleteTasks(taskId)
}