package com.gunalirezqi.moviecatalogue.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gunalirezqi.moviecatalogue.R
import com.gunalirezqi.moviecatalogue.model.FavoriteMovie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie.view.*

class FavoriteMovieAdapter(
    private val items: List<FavoriteMovie>,
    private val listener: (FavoriteMovie) -> Unit
) :
    RecyclerView.Adapter<FavoriteMovieAdapter.FavoriteMovieViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): FavoriteMovieViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_movie, viewGroup, false)
        return FavoriteMovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteMovieViewHolder, position: Int) {
        holder.bindItem(items[position], listener)
    }

    override fun getItemCount(): Int = items.size

    inner class FavoriteMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(
            items: FavoriteMovie,
            listener: (FavoriteMovie) -> Unit
        ) {
            with(itemView) {

                // Set Poster
                movie_poster.clipToOutline = true
                Picasso.get().load("https://image.tmdb.org/t/p/original/${items.moviePoster}")
                    .into(movie_poster)

                // Set Title
                movie_title.text = items.movieTitle

                // Set Release Date
                if (items.movieReleaseDate.isNullOrEmpty()) {
                    movie_release_date.text = resources.getString(R.string.empty_release_date)
                } else {
                    movie_release_date.text = items.movieReleaseDate.substring(0, 4)
                }

                // Set RatingBar
                movie_vote_average.text = items.movieVoteAverage
                movie_ratingbar_vote_average.rating =
                    (items.movieVoteAverage?.toFloat()?.div(10)?.times(5)) ?: 0.0F

                // Set Overview
                if (items.movieOverview.isNullOrBlank()) {
                    movie_overview.text = resources.getString(R.string.empty_overview)
                } else {
                    movie_overview.text = items.movieOverview
                }

                itemView.setOnClickListener {
                    listener(items)
                }
            }
        }
    }
}