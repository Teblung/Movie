package com.teblung.movie.view.movie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teblung.movie.R
import com.teblung.movie.databinding.ActivityMovieBinding
import com.teblung.movie.model.remote.response.DiscoverMovieResponse
import com.teblung.movie.viewmodel.DiscoverViewModel

class MovieActivity : AppCompatActivity() {

    private val binding: ActivityMovieBinding by lazy {
        ActivityMovieBinding.inflate(layoutInflater)
    }

    private val viewModel: DiscoverViewModel by lazy {
        ViewModelProvider(this)[DiscoverViewModel::class.java]
    }

    private lateinit var movieAdapter: MovieAdapter
    private lateinit var genre: String

    private var isLoading = false
    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupUI()
        setupData()
    }

    private fun setupData() {
        genre = intent.getStringExtra(GENRE) ?: ""

        viewModel.apply {
            getDiscoverMovie(this@MovieActivity, currentPage, genre)
            observeDiscoverMovieLiveData().observe(
                this@MovieActivity
            ) {
                movieAdapter.setData(it)
                Log.d(TAG, "Size Main ${it.size}")
            }
        }
    }

    private fun loadingData(state: Boolean) {
        binding.apply {
            if (state) {
                tvTitle.visibility = View.VISIBLE
                tvTitle.text = getString(R.string.loading_text)
            } else {
                tvTitle.visibility = View.GONE
            }
        }
    }

    private fun setupUI() {
        binding.apply {
            movieAdapter = MovieAdapter()
            rvMovie.apply {
                val gridLayout = GridLayoutManager(this@MovieActivity, 2)
                layoutManager = gridLayout
                adapter = movieAdapter
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        val visibleItemCount = gridLayout.childCount
                        val totalItemCount = gridLayout.itemCount
                        val firstVisibleItemPosition = gridLayout.findFirstVisibleItemPosition()

                        Log.d(TAG, "visible $visibleItemCount || total $totalItemCount || first $firstVisibleItemPosition")
                        // Trigger loading more data when nearing the end of the list
                        val threshold = 0 // Adjust this value as needed
                        if (!isLoading && totalItemCount - visibleItemCount <= firstVisibleItemPosition + threshold) {
                            currentPage++
                            loadMoreData(currentPage)
                        }
                    }
                })
            }
        }
    }

    private fun loadMoreData(currentPage: Int) {
        isLoading = true
        loadingData(true)
        // Perform an API request or load data from a data source
        // Here, we simulate loading data by delaying for a few seconds
        Handler(Looper.getMainLooper()).postDelayed({
            val updateData =
                fetchNextData(currentPage) // Replace with your actual data retrieval logic
            Log.d("Movie", "Data ${updateData.size}")
            movieAdapter.setData(updateData)

            isLoading = false
            loadingData(false)
        }, 2000) // Adjust the delay as needed
    }

    private fun fetchNextData(currentPage: Int): MutableList<DiscoverMovieResponse.Result> {
        // Implement your logic to fetch the next set of data based on the current page and page size
        // For example, make an API request and return the result
        // Here, we'll simulate data by generating random genre names
        val newData = mutableListOf<DiscoverMovieResponse.Result>()
        viewModel.apply {
            getDiscoverMovie(this@MovieActivity, currentPage, genre)
            observeDiscoverMovieLiveData().observe(
                this@MovieActivity
            ) { newData.addAll(it) }
        }
        return newData
    }

    companion object {

        private const val GENRE = "GENRE"
        private val TAG = MovieActivity::class.java.simpleName

        fun intentToMovieActivity(context: Context, genre: String): Intent {
            return Intent(context, MovieActivity::class.java).apply {
                putExtra(GENRE, genre)
            }
        }
    }
}