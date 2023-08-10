package com.teblung.movie.model.local.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detail_movie")
data class DetailMovie(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "poster_path")
    val posterPath: String,

    @ColumnInfo(name = "original_title")
    val originalTitle: String,

    @ColumnInfo(name = "tagline")
    val tagline: String,

    @ColumnInfo(name = "overview")
    val overview: String,

    @ColumnInfo(name = "bookmarked")
    var bookmarked: Boolean = false
)