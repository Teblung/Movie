package com.teblung.movie.view.detail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.recyclerview.widget.RecyclerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.teblung.movie.R
import com.teblung.movie.databinding.ItemTrailerBinding
import com.teblung.movie.model.remote.response.TrailerMovieResponse

class TrailerAdapter(
    private val lifecycle: Lifecycle
) :
    RecyclerView.Adapter<TrailerAdapter.ViewHolder>() {

    private var listData = listOf<TrailerMovieResponse.Result>()

    fun setData(data: List<TrailerMovieResponse.Result>) {
        listData = data
        Log.d("Trailer", "$listData")
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), LifecycleObserver {
        private val binding = ItemTrailerBinding.bind(view)

        init {
            lifecycle.addObserver(this)
        }

        fun bind(item: TrailerMovieResponse.Result) {
            binding.apply {
                youtubePlayerView.apply {
                    getYouTubePlayerWhenReady(object : YouTubePlayerCallback {
                        override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                            youTubePlayer.cueVideo(item.key, 0f)
                        }
                    })
                }
                tvName.text = item.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_trailer, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TrailerAdapter.ViewHolder, position: Int) {
        holder.bind(listData[position])
    }

    override fun getItemCount(): Int = listData.size

    companion object {
        private val TAG = TrailerAdapter::class.java.simpleName
    }
}