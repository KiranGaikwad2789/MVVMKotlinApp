package com.example.mvvmkotlinapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mvvmkotlinapp.repository.FirebaseChatRepository
import com.example.mvvmkotlinapp.repository.ProductListRepository

class FirebaseChatViewModel (application: Application) : AndroidViewModel(application) {

    private var repository: FirebaseChatRepository = FirebaseChatRepository(application)

    fun getChatUsersList() = repository.getlist()

}