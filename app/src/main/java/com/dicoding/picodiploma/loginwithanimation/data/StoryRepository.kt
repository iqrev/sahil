package com.dicoding.picodiploma.loginwithanimation.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.ApiService
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.response.DetailStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.response.ErrorResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.response.FileUploadResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.response.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.response.StoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class StoryRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
){
    fun postUserLogin(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try{
            val response = apiService.login(email, password)
            emit(Result.Success(response))
        } catch (e: HttpException){
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        }
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout(){
        userPreference.logout()
    }

    fun postUserRegister(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try{
            val response = apiService.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: HttpException){
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        }
    }

    fun getAllStories(token: String): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStories("Bearer $token")
            emit(Result.Success(response))
        } catch (e: HttpException){
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        }
    }

    fun getDetailStories(token: String, idStory: String): LiveData<Result<DetailStoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailStories("Bearer $token", idStory)
            emit(Result.Success(response))
        } catch (e: HttpException){
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        }
    }

    fun uploadImage(token: String, file: MultipartBody.Part, description: RequestBody): LiveData<Result<FileUploadResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.uploadImage("Bearer $token", file, description)
            emit(Result.Success(response))
        } catch (e: HttpException){
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.message
            emit(Result.Error(errorMessage.toString()))
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(userPreference, apiService)
            }.also { instance = it }
    }
}