package com.teblung.movie.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.teblung.movie.BuildConfig.API_KEY
import com.teblung.movie.model.remote.response.DiscoverMovieResponse
import com.teblung.movie.model.remote.service.ApiClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class DiscoverViewModel() : ViewModel() {

    private val discoverMovieLiveData = MutableLiveData<List<DiscoverMovieResponse.Result>>()

    fun getDiscoverMovie(context: Context, page: Int, genre: String) {
        ApiClient().getApiService(context).getDiscoverMovie("Bearer $API_KEY", page, genre)
            .enqueue(object : Callback<DiscoverMovieResponse?> {
                override fun onResponse(
                    call: Call<DiscoverMovieResponse?>,
                    response: Response<DiscoverMovieResponse?>
                ) {
                    if (response.isSuccessful) {
                        discoverMovieLiveData.value = response.body()?.results
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

                override fun onFailure(call: Call<DiscoverMovieResponse?>, t: Throwable) {
                    if (t is HttpException) {
                        val errorResponse = t.response()
                        val errorCode = errorResponse?.code()
                        Log.d("Genre", "onFailure $errorResponse || $errorCode")
                    }
                }
            })
    }

    fun observeDiscoverMovieLiveData(): LiveData<List<DiscoverMovieResponse.Result>> =
        discoverMovieLiveData
}