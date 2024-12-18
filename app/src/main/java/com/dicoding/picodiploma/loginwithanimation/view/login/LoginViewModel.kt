package com.dicoding.picodiploma.loginwithanimation.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun postUserLogin(email: String, password: String) = storyRepository.postUserLogin(email, password)
    fun saveSession(user: UserModel){
        viewModelScope.launch {
            storyRepository.saveSession(user)
        }
    }

}