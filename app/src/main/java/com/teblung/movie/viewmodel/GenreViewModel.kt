package com.teblung.movie.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teblung.movie.BuildConfig.API_KEY
import com.teblung.movie.model.remote.response.GenreMovieListResponse
import com.teblung.movie.model.remote.service.ApiClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class GenreViewModel : ViewModel() {

    private var genreMovieLiveData = MutableLiveData<List<GenreMovieListResponse.Genre>>()

    fun getGenreMovie(context: Context) {
        ApiClient().getApiService(context).getGenreMovie("Bearer $API_KEY")
            .enqueue(object : Callback<GenreMovieListResponse?> {
                override fun onResponse(
                    call: Call<GenreMovieListResponse?>,
                    response: Response<GenreMovieListResponse?>
                ) {
                    if (response.isSuccessful) {
                        genreMovieLiveData.value = response.body()?.genres
                    } else {
                        try {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            Toast.makeText(
                                context,
                                jObjError.getString("status_message"),
                                Toast.LENGTH_LONG
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }

                override fun onFailure(call: Call<GenreMovieListResponse?>, t: Throwable) {
                    if (t is HttpException) {
                        val errorResponse = t.response()
                        val errorCode = errorResponse?.code()
                        Log.d("Genre", "onFailure $errorResponse || $errorCode")
                    }
                }
            })
    }

    fun observeGenreMovieLiveData(): LiveData<List<GenreMovieListResponse.Genre>> =
        genreMovieLiveData
}