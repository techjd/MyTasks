package com.techjd.mytasks.di

import com.techjd.mytasks.data.remote.TasksApi
import com.techjd.mytasks.data.repository.TaskRepositoryImpl
import com.techjd.mytasks.domain.repository.ITasksRepository
import com.techjd.mytasks.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  @Provides
  @Singleton
  fun providesRetrofit(client: OkHttpClient): Retrofit = Retrofit
    .Builder()
    .baseUrl(Constants.BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .client(client)
    .build()

  @Provides
  @Singleton
  fun providesTaskApi(retrofit: Retrofit): TasksApi = retrofit.create(TasksApi::class.java)

  @Provides
  @Singleton
  fun providesOkHttp(loggingInterceptor: HttpLoggingInterceptor) = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .build()

  @Provides
  @Singleton
  fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
  }

  @Provides
  @Singleton
  fun providesTaskRepository(api: TasksApi): ITasksRepository = TaskRepositoryImpl(api)
}