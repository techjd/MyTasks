package com.techjd.mytasks.data.repository

import com.techjd.mytasks.data.NetworkResult
import com.techjd.mytasks.data.remote.TasksApi
import com.techjd.mytasks.data.remote.dto.response.success.SuccessDto
import com.techjd.mytasks.data.remote.dto.response.tasks.TasksDto
import com.techjd.mytasks.domain.repository.ITasksRepository
import com.techjd.mytasks.util.Constants
import java.net.HttpURLConnection
import kotlinx.coroutines.test.runTest
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@DisplayName("Task Repository Test")
class TaskRepositoryTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl
    private lateinit var service: TasksApi
    private lateinit var taskRepository: ITasksRepository
    private val gsonConverterFactory = GsonConverterFactory.create()
    private val tasksJson = TestConstants.tasksJson

    @BeforeEach
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url(Constants.BASE_URL)

        service = Retrofit
          .Builder()
          .baseUrl(baseUrl)
          .addConverterFactory(gsonConverterFactory)
          .build()
          .create(TasksApi::class.java)

        taskRepository = TaskRepositoryImpl(
          tasksApi = service
        )
    }

    @Test
    @DisplayName("Get All Tasks - Success")
    fun getAllTasksSuccess() = runTest {
        mockWebServer.enqueue(
          MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(tasksJson)
        )

        val items = taskRepository.getAllTasks() as NetworkResult.Success<TasksDto>

        Assertions.assertNotNull(items)
        Assertions.assertEquals(2273, items.data.tasks.first().taskId)
    }

    @Test
    @DisplayName("Add Task - Success")
    fun addTaskSuccess() = runTest {
        mockWebServer.enqueue(
          MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(TestConstants.successJson)
        )

        val response = taskRepository.addTasks("Hello World", 1L, description = null) as NetworkResult.Success<SuccessDto>

        Assertions.assertNotNull(response)
        Assertions.assertEquals("Success", response.data.status)
    }

    @Test
    @DisplayName("Delete Task - Success")
    fun deleteTaskSuccess() = runTest {
        mockWebServer.enqueue(
          MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(TestConstants.successJson)
        )

        val response = taskRepository.deleteTasks(12) as NetworkResult.Success<SuccessDto>

        Assertions.assertNotNull(response)
        Assertions.assertEquals("Success", response.data.status)
    }

    @AfterEach
    fun tearDown() {
        println("Tear Down")
        mockWebServer.shutdown()
    }
}