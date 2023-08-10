package com.teblung.movie.view.genre

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.teblung.movie.databinding.ActivityGenreBinding
import com.teblung.movie.view.favourite.FavouriteActivity
import com.teblung.movie.viewmodel.GenreViewModel

class GenreActivity : AppCompatActivity() {

    private val binding: ActivityGenreBinding by lazy {
        ActivityGenreBinding.inflate(layoutInflater)
    }

    private val viewModel: GenreViewModel by lazy {
        ViewModelProvider(this)[GenreViewModel::class.java]
    }

    private lateinit var genreAdapter: GenreAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupUI()
        setupData()
    }

    private fun setupData() {
        viewModel.apply {
            getGenreMovie(this@GenreActivity)
            observeGenreMovieLiveData().observe(
                this@GenreActivity
            ) { genreAdapter.setData(it) }
        }
    }

    private fun setupUI() {
        binding.apply {
            btnShowBookmark.apply {
                text = "Favourite Movie"
                setOnClickListener {
                    startActivity(FavouriteActivity.intentToFavouriteActivity(this@GenreActivity))
                }
            }
            genreAdapter = GenreAdapter()
            rvGenre.apply {
                layoutManager = LinearLayoutManager(this@GenreActivity)
                adapter = genreAdapter
            }
        }
    }
}