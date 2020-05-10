package com.gunalirezqi.moviecatalogue.feature.search

import com.google.gson.Gson
import com.gunalirezqi.moviecatalogue.R
import com.gunalirezqi.moviecatalogue.api.ApiRepository
import com.gunalirezqi.moviecatalogue.api.TheMovieDBApi
import com.gunalirezqi.moviecatalogue.model.MovieResponse
import com.gunalirezqi.moviecatalogue.model.TvShowResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchPresenter(
    private val view: SearchActivity,
    private val apiRepository: ApiRepository,
    private val gson: Gson
) {
    fun getMovieList(query: String?) {
        view.showLoading()
        view.handleNotFound(false, "movies")

        GlobalScope.launch(Dispatchers.Main) {
            val language = view.getString(R.string.language)
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheMovieDBApi.getMovieSearch(language, query)),
                MovieResponse::class.java
            )

            view.hideLoading()
            if (data.results.isEmpty()) {
                view.handleNotFound(true, "movies")
            } else {
                view.showMovieList(data.results)
            }
        }
    }

    fun getTvShowList(query: String?) {
        view.showLoading()
        view.handleNotFound(false, "tv_shows")

        GlobalScope.launch(Dispatchers.Main) {
            val language = view.getString(R.string.language)
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheMovieDBApi.getTvShowSearch(language, query)),
                TvShowResponse::class.java
            )

            view.hideLoading()
            if (data.results.isEmpty()) {
                view.handleNotFound(true, "tv_shows")
            } else {
                view.showTvShowList(data.results)
            }
        }
    }
}