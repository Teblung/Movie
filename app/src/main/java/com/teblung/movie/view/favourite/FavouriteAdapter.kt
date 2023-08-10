package com.teblung.movie.view.favourite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.teblung.movie.R
import com.teblung.movie.databinding.ItemMovieBinding
import com.teblung.movie.model.local.entity.DetailMovie
import com.teblung.movie.model.remote.response.DiscoverMovieResponse
import com.teblung.movie.view.detail.DetailActivity

class FavouriteAdapter() : RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {

    private var listData = mutableListOf<DetailMovie>()

    fun setData(data: List<DetailMovie>) {
        listData.clear()
        listData.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemMovieBinding.bind(view)
        fun bind(item: DetailMovie) {
            binding.apply {
                Glide.with(itemView.context)
                    .load("${BASE_URL_IMAGE}${item.posterPath}")
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.ic_loading)
                            .error(R.drawable.ic_error)
                    ).into(imgMovie)
                tvTitleMovie.text = item.originalTitle
                itemView.setOnClickListener {
                    itemView.context.startActivity(
                        DetailActivity.intentToDetailActivity(itemView.context, item.id)
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FavouriteAdapter.ViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    companion object {
        const val BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w300"
    }
}