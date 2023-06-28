package com.teblung.movie.view.genre

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teblung.movie.R
import com.teblung.movie.databinding.ItemGenreBinding
import com.teblung.movie.model.remote.response.GenreMovieListResponse
import com.teblung.movie.view.movie.MovieActivity

class GenreAdapter() :
    RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    private var listData = listOf<GenreMovieListResponse.Genre>()

    fun setData(data: List<GenreMovieListResponse.Genre>) {
        this.listData = data
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemGenreBinding.bind(view)
        fun bind(item: GenreMovieListResponse.Genre) {
            binding.apply {
                tvGenreName.text = item.name
                itemView.setOnClickListener {
                    itemView.context.startActivity(
                        MovieActivity.intentToMovieActivity(itemView.context, item.name)
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_genre, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GenreAdapter.ViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size
}