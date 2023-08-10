package com.teblung.movie.view.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.teblung.movie.R
import com.teblung.movie.databinding.ActivityDetailBinding
import com.teblung.movie.model.local.LocalDataSource
import com.teblung.movie.model.local.entity.DetailMovie
import com.teblung.movie.model.local.room.MovieDatabase
import com.teblung.movie.model.remote.response.ReviewMovieResponse
import com.teblung.movie.view.movie.MovieAdapter
import com.teblung.movie.viewmodel.DetailViewModel
import com.teblung.movie.viewmodel.RoomDetailViewModel
import com.teblung.movie.viewmodel.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    private val repository: LocalDataSource by lazy {
        LocalDataSource(MovieDatabase.getInstance(this).movieDao())
    }

    private val viewModel: DetailViewModel by lazy {
        ViewModelProvider(this)[DetailViewModel::class.java]
    }

    private val favViewModel: RoomDetailViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory(repository))[RoomDetailViewModel::class.java]
    }

    private lateinit var trailerAdapter: TrailerAdapter
    private lateinit var reviewAdapter: ReviewAdapter

    private var movieId = 0
    private var isLoading = false
    private var currentPage = 1
    private var id = 0
    private var detailMovie: DetailMovie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupUI()
        setupData()
    }

    private fun setupData() {
        movieId = intent.getIntExtra(MOVIEID, 0)

        viewModel.apply {
            getDetailMovie(this@DetailActivity, movieId)
            observeDetailMovieLiveData().observe(this@DetailActivity) {
                id = it.id
                detailMovie = DetailMovie(
                    id = it.id,
                    posterPath = it.posterPath,
                    originalTitle = it.originalTitle,
                    tagline = it.tagline,
                    overview = it.overview
                )
                binding.apply {
                    Glide.with(this@DetailActivity)
                        .load("${MovieAdapter.BASE_URL_IMAGE}${it.posterPath}")
                        .apply(
                            RequestOptions.placeholderOf(R.drawable.ic_loading)
                                .error(R.drawable.ic_error)
                        ).into(imgDetail)

                    tvTitle.text = it.originalTitle
                    tvTagLine.text = it.tagline
                    tvOverview.text = it.overview
                }
                favViewModel.getMovieDetail(id).observe(this@DetailActivity) { data ->
                    Log.d("Bookmark", "Data $data")
                    binding.btnBookmark.apply {
                        text = if (data?.bookmarked == true) {
                            "UnBookmark"
                        } else {
                            "Bookmark"
                        }
                    }
                    binding.btnBookmark.setOnClickListener {
                        Log.d("Bookmark", "State ${detailMovie?.bookmarked} || ${detailMovie?.originalTitle}")
                        if (data != null) {
                            if (!data.bookmarked) {
                                detailMovie!!.bookmarked = true
                                favViewModel.insert(detailMovie!!)
                                binding.btnBookmark.text = "UnBookmark"
                                Toast.makeText(this@DetailActivity, "Bookmark Success", Toast.LENGTH_SHORT).show()
                            } else {
                                data.bookmarked = false
                                favViewModel.delete(data.id)
                                binding.btnBookmark.text = "Bookmark"
                                Toast.makeText(this@DetailActivity, "UnBookmark Success", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            if (!detailMovie!!.bookmarked) {
                                detailMovie!!.bookmarked = true
                                favViewModel.insert(detailMovie!!)
                                binding.btnBookmark.text = "UnBookmark"
                                Toast.makeText(this@DetailActivity, "Bookmark Success", Toast.LENGTH_SHORT).show()
                            } else {
                                detailMovie!!.bookmarked = false
                                favViewModel.delete(detailMovie!!.id)
                                binding.btnBookmark.text = "Bookmark"
                                Toast.makeText(this@DetailActivity, "UnBookmark Success", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }

            getReview(this@DetailActivity, movieId, currentPage)
            observeReviewMovieLiveData().observe(this@DetailActivity) {
                reviewAdapter.setData(it)
                if (it.isEmpty()) {
                    Toast.makeText(this@DetailActivity, "No Data Review", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            getTrailerMovie(this@DetailActivity, movieId)
            observeTrailerMovieLiveData().observe(this@DetailActivity) {
                trailerAdapter.setData(it)
                if (it.isEmpty()) {
                    Toast.makeText(this@DetailActivity, "No Data Trailer", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun setupUI() {
        binding.apply {
            tvReview.text = "All Review"
            tvVideo.text = "Recomendations"

            reviewAdapter = ReviewAdapter()
            trailerAdapter = TrailerAdapter(lifecycle)

            rvReview.apply {
                val linearLayout =
                    LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
                adapter = reviewAdapter
                layoutManager = linearLayout
                addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        val visibleItemCount = linearLayout.childCount
                        val totalItemCount = linearLayout.itemCount
                        val firstVisibleItemPosition = linearLayout.findFirstVisibleItemPosition()

                        Log.d(
                            TAG,
                            "visible $visibleItemCount || total $totalItemCount || first $firstVisibleItemPosition"
                        )
                        // Trigger loading more data when nearing the end of the list
                        val threshold = 0 // Adjust this value as needed
                        if (!isLoading && totalItemCount - visibleItemCount <= firstVisibleItemPosition + threshold) {
                            currentPage++
                            loadMoreData(currentPage)
                        }
                    }
                })
            }

            rvTrailer.apply {
                adapter = trailerAdapter
                layoutManager = LinearLayoutManager(this@DetailActivity)
            }
        }
    }

    private fun loadingData(state: Boolean) {
        binding.apply {
            if (state) {
                tvLoading.visibility = View.VISIBLE
                tvLoading.text = getString(R.string.loading_text)
            } else {
                tvLoading.visibility = View.GONE
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
            reviewAdapter.setData(updateData)

            isLoading = false
            loadingData(false)
        }, 2000) // Adjust the delay as needed
    }

    private fun fetchNextData(currentPage: Int): MutableList<ReviewMovieResponse.Result> {
        // Implement your logic to fetch the next set of data based on the current page and page size
        // For example, make an API request and return the result
        // Here, we'll simulate data by generating random genre names
        val newData = mutableListOf<ReviewMovieResponse.Result>()
        viewModel.apply {
            getReview(this@DetailActivity, movieId, currentPage)
            observeReviewMovieLiveData().observe(
                this@DetailActivity
            ) { newData.addAll(it) }
        }
        return newData
    }

    companion object {

        private val TAG = DetailActivity::class.java.simpleName

        private const val MOVIEID = "MOVIEID"

        fun intentToDetailActivity(context: Context, movieId: Int): Intent {
            return Intent(context, DetailActivity::class.java).apply {
                putExtra(MOVIEID, movieId)
            }
        }
    }
}