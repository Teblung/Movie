package com.teblung.movie.model.local

import com.teblung.movie.model.local.entity.DetailMovie
import com.teblung.movie.model.local.room.MovieDao
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val movieDao: MovieDao) {
    fun insert(detailMovie: DetailMovie) {
        movieDao.insert(detailMovie)
    }

    fun delete(id: Int) {
        movieDao.delete(id)
    }

    fun readAllMovie(): Flow<List<DetailMovie>> {
        return movieDao.readAllMovie()
    }

    fun getMovieDetail(id: Int): Flow<DetailMovie?> {
        return movieDao.getMovieDetail(id)
    }
}