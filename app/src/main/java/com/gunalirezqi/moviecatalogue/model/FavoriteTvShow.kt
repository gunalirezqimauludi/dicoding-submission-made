package com.gunalirezqi.moviecatalogue.model

data class FavoriteTvShow(
    val id: Long?,
    val tvShowId: String?,
    val tvShowName: String?,
    val tvShowOverview: String?,
    val tvShowFirstAirDate: String?,
    val tvShowVoteAverage: String?,
    val tvShowPoster: String?,
    val tvShowBackdrop: String?,
    val tvShowGenre: String?
) {

    companion object {
        const val TABLE_FAVORITE: String = "TABLE_FAVORITE"
        const val ID: String = "ID_"
        const val TVSHOW_ID: String = "TVSHOW_ID"
        const val TVSHOW_NAME: String = "TVSHOW_NAME"
        const val TVSHOW_OVERVIEW: String = "TVSHOW_OVERVIEW"
        const val TVSHOW_FIRST_AIR_DATE: String = "TVSHOW_FIRST_AIR_DATE"
        const val TVSHOW_VOTE_AVERAGE: String = "TVSHOW_VOTE_AVERAGE"
        const val TVSHOW_POSTER_PATH: String = "TVSHOW_POSTER_PATH"
        const val TVSHOW_BACKDROP_PATH: String = "TVSHOW_BACKDROP_PATH"
        const val TVSHOW_GENRE: String = "TVSHOW_GENRE"
    }
}