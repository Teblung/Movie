package com.teblung.movie.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.teblung.movie.model.local.LocalDataSource
import com.teblung.movie.model.local.entity.DetailMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomDetailViewModel(private val localDataSource: LocalDataSource) : ViewModel() {

    fun insert(detailMovie: DetailMovie) {
        viewModelScope.launch(Dispatchers.IO) {
            localDataSource.insert(detailMovie)
        }
    }

    fun delete(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            localDataSource.delete(id)
        }
    }

    val allDetailMovie: LiveData<List<DetailMovie>> = localDataSource.readAllMovie().asLiveData()

    fun getMovieDetail(id: Int): LiveData<DetailMovie?> {
        return localDataSource.getMovieDetail(id).asLiveData()
    }
}