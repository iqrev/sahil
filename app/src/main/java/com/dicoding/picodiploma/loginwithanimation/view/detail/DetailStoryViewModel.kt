package com.dicoding.picodiploma.loginwithanimation.view.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel

class DetailStoryViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun getDetailStories(token: String, idStory: String) = storyRepository.getDetailStories(token, idStory)

    fun getSession(): LiveData<UserModel> {
        return storyRepository.getSession().asLiveData()
    }
}