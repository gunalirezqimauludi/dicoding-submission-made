package com.gunalirezqi.moviecatalogue.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Genres(
    @SerializedName("id")
    var genreId: String? = null,

    @SerializedName("name")
    var genreName: String? = null
) : Parcelable