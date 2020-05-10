package com.gunalirezqi.consumerapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    val id: Long?,
    val movieId: String?,
    val movieTitle: String?,
    val movieOverview: String?,
    val movieReleaseDate: String?,
    val movieVoteAverage: String?,
    val moviePoster: String?,
    val movieBackdrop: String?,
    val movieGenre: String?
) : Parcelable {

    companion object {
        const val TABLE_FAVORITE: String = "TABLE_FAVORITE"
        const val ID: String = "ID_"
        const val MOVIE_ID: String = "MOVIE_ID"
        const val MOVIE_TITLE: String = "MOVIE_TITLE"
        const val MOVIE_OVERVIEW: String = "MOVIE_OVERVIEW"
        const val MOVIE_RELEASE_DATE: String = "MOVIE_RELEASE_DATE"
        const val MOVIE_VOTE_AVERAGE: String = "MOVIE_VOTE_AVERAGE"
        const val MOVIE_POSTER_PATH: String = "MOVIE_POSTER_PATH"
        const val MOVIE_BACKDROP_PATH: String = "MOVIE_BACKDROP_PATH"
        const val MOVIE_GENRE: String = "MOVIE_GENRE"
    }
}