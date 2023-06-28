package com.teblung.movie.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teblung.movie.BuildConfig.API_KEY
import com.teblung.movie.model.remote.response.DetailMovieResponse
import com.teblung.movie.model.remote.response.ReviewMovieResponse
import com.teblung.movie.model.remote.response.TrailerMovieResponse
import com.teblung.movie.model.remote.service.ApiClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class DetailViewModel() : ViewModel() {

    private val detailMovie = MutableLiveData<DetailMovieResponse>()
    private val reviewMovie = MutableLiveData<List<ReviewMovieResponse.Result>>()
    private val trailerMovie = MutableLiveData<List<TrailerMovieResponse.Result>>()

    fun getDetailMovie(context: Context, movieId: Int) {
        ApiClient().getApiService(context).getDetailMovie("Bearer $API_KEY", movieId)
            .enqueue(object : Callback<DetailMovieResponse?> {
                override fun onResponse(
                    call: Call<DetailMovieResponse?>,
                    response: Response<DetailMovieResponse?>
                ) {
                    if (response.isSuccessful) {
                        detailMovie.value = response.body()
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

                override fun onFailure(call: Call<DetailMovieResponse?>, t: Throwable) {
                    if (t is HttpException) {
                        val errorResponse = t.response()
                        val errorCode = errorResponse?.code()
                        Log.d("Genre", "onFailure $errorResponse || $errorCode")
                    }
                }
            })
    }

    fun getReview(context: Context, movieId: Int) {
        ApiClient().getApiService(context).getReviewMovie("Bearer $API_KEY", movieId)
            .enqueue(object : Callback<ReviewMovieResponse?> {
                override fun onResponse(
                    call: Call<ReviewMovieResponse?>,
                    response: Response<ReviewMovieResponse?>
                ) {
                    if (response.isSuccessful) {
                        reviewMovie.value = response.body()?.results
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

                override fun onFailure(call: Call<ReviewMovieResponse?>, t: Throwable) {
                    if (t is HttpException) {
                        val errorResponse = t.response()
                        val errorCode = errorResponse?.code()
                        Log.d("Genre", "onFailure $errorResponse || $errorCode")
                    }
                }
            })
    }

    fun getTrailerMovie(context: Context, movieId: Int) {
        ApiClient().getApiService(context).getTrailerMovie("Bearer $API_KEY", movieId)
            .enqueue(object : Callback<TrailerMovieResponse?> {
                override fun onResponse(
                    call: Call<TrailerMovieResponse?>,
                    response: Response<TrailerMovieResponse?>
                ) {
                    if (response.isSuccessful) {
                        trailerMovie.value = response.body()?.results
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

                override fun onFailure(call: Call<TrailerMovieResponse?>, t: Throwable) {
                    if (t is HttpException) {
                        val errorResponse = t.response()
                        val errorCode = errorResponse?.code()
                        Log.d("Genre", "onFailure $errorResponse || $errorCode")
                    }
                }
            })
    }

    fun observeDetailMovieLiveData(): LiveData<DetailMovieResponse> = detailMovie
    fun observeReviewMovieLiveData(): LiveData<List<ReviewMovieResponse.Result>> = reviewMovie
    fun observeTrailerMovieLiveData(): LiveData<List<TrailerMovieResponse.Result>> = trailerMovie
}