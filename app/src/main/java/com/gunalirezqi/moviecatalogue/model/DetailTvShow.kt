package com.gunalirezqi.moviecatalogue.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DetailTvShow(
    @SerializedName("id")
    var tvShowId: String? = null,

    @SerializedName("name")
    var tvShowName: String? = null,

    @SerializedName("overview")
    var tvShowOverview: String? = null,

    @SerializedName("first_air_date")
    var tvShowFirstAirDate: String? = null,

    @SerializedName("vote_average")
    var tvShowVoteAverage: String? = null,

    @SerializedName("poster_path")
    var tvShowPoster: String? = null,

    @SerializedName("backdrop_path")
    var tvShowBackdrop: String? = null,

    @SerializedName("genres")
    var tvShowGenre: List<Genres>
) : Parcelable