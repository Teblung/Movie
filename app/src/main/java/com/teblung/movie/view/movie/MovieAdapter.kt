package com.teblung.movie.view.movie

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.teblung.movie.R
import com.teblung.movie.databinding.ItemMovieBinding
import com.teblung.movie.model.remote.response.DiscoverMovieResponse
import com.teblung.movie.view.detail.DetailActivity

class MovieAdapter() : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private var listData = mutableListOf<DiscoverMovieResponse.Result>()

    fun setData(data: List<DiscoverMovieResponse.Result>) {
        val filterData = data.filter { !listData.contains(it) }
        listData.addAll(filterData)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemMovieBinding.bind(view)
        fun bind(item: DiscoverMovieResponse.Result) {
            binding.apply {
                Glide.with(itemView.context)
                    .load("${BASE_URL_IMAGE}${item.posterPath}")
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    ).into(imgMovie)
                tvTitleMovie.text = item.title
                itemView.setOnClickListener {
                    itemView.context.startActivity(
                        DetailActivity.intentToDetailActivity(itemView.context, item.id)
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieAdapter.ViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    companion object {
        const val BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w300"
    }
}