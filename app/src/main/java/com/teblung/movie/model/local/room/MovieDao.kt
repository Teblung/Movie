package com.teblung.movie.model.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.teblung.movie.model.local.entity.DetailMovie
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert
    fun insert(detailMovie: DetailMovie)

    @Query("DELETE FROM detail_movie WHERE id = :id")
    fun delete(id: Int)

    @Query("SELECT * FROM detail_movie WHERE bookmarked = 1")
    fun readAllMovie(): Flow<List<DetailMovie>>

    @Query("SELECT * FROM detail_movie WHERE id = :id")
    fun getMovieDetail(id: Int): Flow<DetailMovie?>
}