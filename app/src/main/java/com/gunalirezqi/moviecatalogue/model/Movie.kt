package com.gunalirezqi.moviecatalogue.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(
    @SerializedName("id")
    var movieId: String? = null,

    @SerializedName("title")
    var movieTitle: String? = null,

    @SerializedName("overview")
    var movieOverview: String? = null,

    @SerializedName("release_date")
    var movieReleaseDate: String? = null,

    @SerializedName("vote_average")
    var movieVoteAverage: String? = null,

    @SerializedName("poster_path")
    var moviePoster: String? = null,

    @SerializedName("backdrop_path")
    var movieBackdrop: String? = null,

    @SerializedName("genre_ids")
    var movieGenre: IntArray
) : Parcelable