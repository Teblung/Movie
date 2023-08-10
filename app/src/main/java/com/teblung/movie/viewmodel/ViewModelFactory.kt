package com.teblung.movie.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.teblung.movie.model.local.LocalDataSource

class ViewModelFactory(private val localDataSource: LocalDataSource) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RoomDetailViewModel(localDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}