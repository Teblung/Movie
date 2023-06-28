package com.teblung.movie.model.remote.service

import com.teblung.movie.model.remote.response.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("genre/movie/list")
    fun getGenreMovie(
        @Header("Authorization") token: String
    ): Call<GenreMovieListResponse>

    @GET("discover/movie")
    fun getDiscoverMovie(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("with_genres") genre: String
    ): Call<DiscoverMovieResponse>

    @GET("movie/{movie_id}")
    fun getDetailMovie(
        @Header("Authorization") token: String,
        @Path("movie_id") movieId: Int
    ): Call<DetailMovieResponse>

    @GET("movie/{movie_id}/reviews")
    fun getReviewMovie(
        @Header("Authorization") token: String,
        @Path("movie_id") movieId: Int
    ): Call<ReviewMovieResponse>

    @GET("movie/{movie_id}/videos")
    fun getTrailerMovie(
        @Header("Authorization") token: String,
        @Path("movie_id") movieId: Int
    ): Call<TrailerMovieResponse>
}