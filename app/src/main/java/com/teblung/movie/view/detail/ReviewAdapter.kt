package com.teblung.movie.view.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.teblung.movie.R
import com.teblung.movie.databinding.ItemReviewBinding
import com.teblung.movie.model.remote.response.ReviewMovieResponse
import java.text.SimpleDateFormat
import java.util.*

class ReviewAdapter() : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    private var listData = mutableListOf<ReviewMovieResponse.Result>()

    fun setData(data: List<ReviewMovieResponse.Result>) {
        val filterData = data.filter { !listData.contains(it) }
        listData.addAll(filterData)
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemReviewBinding.bind(view)
        fun bind(item: ReviewMovieResponse.Result) {
            binding.apply {
                val avatar = if (item.authorDetails.avatarPath != null) item.authorDetails.avatarPath else ""
                val filter = if (avatar.contains("/https://secure.gravatar.com/avatar")) {
                    avatar.replace("/https://secure.gravatar.com/avatar", "")
                } else if (avatar.isEmpty()) {
                    ""
                } else {
                    avatar
                }
                if (avatar.isNotEmpty()) {
                    Glide.with(itemView.context)
                        .load("$BASE_URL_PROFILE$filter")
                        .apply(
                            RequestOptions.placeholderOf(R.drawable.ic_loading)
                                .error(R.drawable.ic_error)
                        ).into(imgReview)
                } else {
                    imgReview.setImageResource(R.color.placeholder)
                }

                tvTitleReview.text = buildString {
                    append("A review by ")
                    append(item.author)
                }
                tvCreateReview.text = buildString {
                    append("Written by ")
                    append(item.author)
                    append(" on ${formatDate(item.createdAt)}")
                }
                tvContentReview.text = item.content
            }
        }

        fun formatDate(dateString: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

            val date = inputFormat.parse(dateString)
            return outputFormat.format(date!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ReviewAdapter.ViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    companion object {
        const val BASE_URL_PROFILE = "https://secure.gravatar.com/avatar"
    }
}