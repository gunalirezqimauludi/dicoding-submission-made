package com.gunalirezqi.moviecatalogue.feature.movie

import com.google.gson.Gson
import com.gunalirezqi.moviecatalogue.R
import com.gunalirezqi.moviecatalogue.api.ApiRepository
import com.gunalirezqi.moviecatalogue.api.TheMovieDBApi
import com.gunalirezqi.moviecatalogue.model.MovieResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MoviePresenter(
    private val view: MovieFragment,
    private val apiRepository: ApiRepository,
    private val gson: Gson
) {
    fun getMovieList() {
        view.showLoading()

        GlobalScope.launch(Dispatchers.Main) {
            val language = view.getString(R.string.language)
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheMovieDBApi.getMovie(language)),
                MovieResponse::class.java
            )

            view.hideLoading()
            if (data.results.isEmpty()) {
                view.handleNotFound()
            } else {
                view.showMovieList(data.results)
            }
        }
    }
}