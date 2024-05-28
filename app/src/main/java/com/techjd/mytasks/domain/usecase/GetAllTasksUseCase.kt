package com.techjd.mytasks.domain.usecase

import com.techjd.mytasks.domain.repository.ITasksRepository
import javax.inject.Inject

class GetAllTasksUseCase @Inject constructor(
  private val iTasksRepository: ITasksRepository
) {
  suspend operator fun invoke() = iTasksRepository.getAllTasks()
}