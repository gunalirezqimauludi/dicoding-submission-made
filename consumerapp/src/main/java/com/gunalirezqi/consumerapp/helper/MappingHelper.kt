package com.gunalirezqi.consumerapp.helper

import android.database.Cursor
import com.gunalirezqi.consumerapp.model.Movie

import java.util.ArrayList

object MappingHelper {

    fun mapCursorToArrayList(moviesCursor: Cursor): ArrayList<Movie> {
        val moviesList = ArrayList<Movie>()

        while (moviesCursor.moveToNext()) {
            val id = moviesCursor.getLong(moviesCursor.getColumnIndexOrThrow(Movie.ID))
            val movieId = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(Movie.MOVIE_ID))
            val movieTitle = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(Movie.MOVIE_TITLE))
            val movieOverview = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(Movie.MOVIE_OVERVIEW))
            val movieReleaseDate = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(Movie.MOVIE_RELEASE_DATE))
            val movieVoteAverage = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(Movie.MOVIE_VOTE_AVERAGE))
            val moviePosterPath = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(Movie.MOVIE_POSTER_PATH))
            val movieBackdropPath = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(Movie.MOVIE_BACKDROP_PATH))
            val movieGenre = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(Movie.MOVIE_GENRE))
            moviesList.add(Movie(id, movieId, movieTitle, movieOverview, movieReleaseDate, movieVoteAverage, moviePosterPath, movieBackdropPath, movieGenre))
        }

        return moviesList
    }

    fun mapCursorToObject(moviesCursor: Cursor): Movie {
        moviesCursor.moveToNext()
        val id = moviesCursor.getLong(moviesCursor.getColumnIndexOrThrow(Movie.ID))
        val movieId = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(Movie.MOVIE_ID))
        val movieTitle = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(Movie.MOVIE_TITLE))
        val movieOverview = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(Movie.MOVIE_OVERVIEW))
        val movieReleaseDate = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(Movie.MOVIE_RELEASE_DATE))
        val movieVoteAverage = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(Movie.MOVIE_VOTE_AVERAGE))
        val moviePosterPath = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(Movie.MOVIE_POSTER_PATH))
        val movieBackdropPath = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(Movie.MOVIE_BACKDROP_PATH))
        val movieGenre = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(Movie.MOVIE_GENRE))
        return Movie(id, movieId, movieTitle, movieOverview, movieReleaseDate, movieVoteAverage, moviePosterPath, movieBackdropPath, movieGenre)
    }
}