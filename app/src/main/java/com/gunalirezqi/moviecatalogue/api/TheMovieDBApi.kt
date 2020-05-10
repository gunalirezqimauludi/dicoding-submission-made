package com.gunalirezqi.moviecatalogue.api

import android.net.Uri
import com.gunalirezqi.moviecatalogue.BuildConfig

object TheMovieDBApi {
    fun getMovie(language: String?): String {
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
            .appendPath("3")
            .appendPath("discover")
            .appendPath("movie")
            .appendQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
            .appendQueryParameter("language", language)
            .build()
            .toString()
    }

    fun getTvShow(language: String?): String {
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
            .appendPath("3")
            .appendPath("discover")
            .appendPath("tv")
            .appendQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
            .appendQueryParameter("language", language)
            .build()
            .toString()
    }

    fun getMovieDetails(movieId: String?, language: String?): String {
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
            .appendPath("3")
            .appendPath("movie")
            .appendPath(movieId)
            .appendQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
            .appendQueryParameter("language", language)
            .build()
            .toString()
    }

    fun getTvShowDetails(tvShowId: String?, language: String?): String {
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
            .appendPath("3")
            .appendPath("tv")
            .appendPath(tvShowId)
            .appendQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
            .appendQueryParameter("language", language)
            .build()
            .toString()
    }

    fun getMovieCredits(movieId: String?, language: String?): String {
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
            .appendPath("3")
            .appendPath("movie")
            .appendPath(movieId)
            .appendPath("credits")
            .appendQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
            .appendQueryParameter("language", language)
            .build()
            .toString()
    }

    fun getTvShowCredits(tvShowId: String?, language: String?): String {
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
            .appendPath("3")
            .appendPath("tv")
            .appendPath(tvShowId)
            .appendPath("credits")
            .appendQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
            .appendQueryParameter("language", language)
            .build()
            .toString()
    }

    fun getMovieSearch(language: String?, query: String?): String {
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
            .appendPath("3")
            .appendPath("search")
            .appendPath("movie")
            .appendQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
            .appendQueryParameter("language", language)
            .appendQueryParameter("query", query)
            .build()
            .toString()
    }

    fun getTvShowSearch(language: String?, query: String?): String {
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
            .appendPath("3")
            .appendPath("search")
            .appendPath("tv")
            .appendQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
            .appendQueryParameter("language", language)
            .appendQueryParameter("query", query)
            .build()
            .toString()
    }

    fun getMovieRelease(date: String?): String {
        return Uri.parse(BuildConfig.BASE_URL).buildUpon()
            .appendPath("3")
            .appendPath("discover")
            .appendPath("movie")
            .appendQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
            .appendQueryParameter("primary_release_date.gte", date)
            .appendQueryParameter("primary_release_date.lte", date)
            .build()
            .toString()
    }
}