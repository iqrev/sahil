package com.dicoding.picodiploma.loginwithanimation.view.addphoto

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddPhotoViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun uploadImage(token: String, file: MultipartBody.Part, description: RequestBody) = storyRepository.uploadImage(token, file, description)

    fun getSession(): LiveData<UserModel> {
        return storyRepository.getSession().asLiveData()
    }
}