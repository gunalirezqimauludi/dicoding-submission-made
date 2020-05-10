package com.gunalirezqi.consumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gunalirezqi.consumerapp.R
import com.gunalirezqi.consumerapp.model.Movie
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_movie.view.*

class MovieAdapter :
    RecyclerView.Adapter<MovieAdapter.FavoriteMovieViewHolder>() {

    var listMovies = ArrayList<Movie>()
        set(listMovies) {
            this.listMovies.clear()
            this.listMovies.addAll(listMovies)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): FavoriteMovieViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_movie, viewGroup, false)
        return FavoriteMovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteMovieViewHolder, position: Int) {
        holder.bindItem(listMovies[position])
    }

    override fun getItemCount(): Int = listMovies.size

    inner class FavoriteMovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItem(
            items: Movie
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
                    movie_overview.text = resources.getString(R.string.empty_movies)
                } else {
                    movie_overview.text = items.movieOverview
                }
            }
        }
    }
}