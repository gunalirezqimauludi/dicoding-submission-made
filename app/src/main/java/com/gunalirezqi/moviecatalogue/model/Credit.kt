package com.gunalirezqi.moviecatalogue.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Credit(
    @SerializedName("name")
    var crewName: String? = null,

    @SerializedName("job")
    var crewJob: String? = null
) : Parcelable