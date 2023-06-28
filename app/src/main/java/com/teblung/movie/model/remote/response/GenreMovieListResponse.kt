package com.teblung.movie.model.remote.response


import com.google.gson.annotations.SerializedName

data class GenreMovieListResponse(
    @SerializedName("genres")
    val genres: List<Genre>
) {
    data class Genre(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String
    )
}