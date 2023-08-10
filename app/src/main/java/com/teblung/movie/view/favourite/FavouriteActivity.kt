package com.teblung.movie.view.favourite

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.teblung.movie.databinding.ActivityFavouriteBinding
import com.teblung.movie.model.local.LocalDataSource
import com.teblung.movie.model.local.room.MovieDatabase
import com.teblung.movie.viewmodel.RoomDetailViewModel
import com.teblung.movie.viewmodel.ViewModelFactory

class FavouriteActivity : AppCompatActivity() {

    private val binding: ActivityFavouriteBinding by lazy {
        ActivityFavouriteBinding.inflate(layoutInflater)
    }

    private val repository: LocalDataSource by lazy {
        LocalDataSource(MovieDatabase.getInstance(this).movieDao())
    }

    private val favViewModel: RoomDetailViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory(repository))[RoomDetailViewModel::class.java]
    }

    private lateinit var favouriteAdapter: FavouriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupUI()
    }

    private fun setupUI() {
        binding.apply {
            favouriteAdapter = FavouriteAdapter()
            rvFavMovie.apply {
                adapter = favouriteAdapter
                layoutManager = LinearLayoutManager(this@FavouriteActivity)
            }
            favViewModel.allDetailMovie.observe(this@FavouriteActivity) {
                favouriteAdapter.setData(it)
                Log.d("Bookmark", "Ini List $it")
                if (it.isEmpty()) {
                    Toast.makeText(this@FavouriteActivity, "No Data Favourite", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    companion object {
        fun intentToFavouriteActivity(context: Context): Intent {
            return Intent(context, FavouriteActivity::class.java)
        }
    }
}