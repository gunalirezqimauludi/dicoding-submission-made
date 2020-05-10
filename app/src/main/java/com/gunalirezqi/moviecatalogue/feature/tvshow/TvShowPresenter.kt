package com.gunalirezqi.moviecatalogue.feature.tvshow

import com.google.gson.Gson
import com.gunalirezqi.moviecatalogue.R
import com.gunalirezqi.moviecatalogue.api.ApiRepository
import com.gunalirezqi.moviecatalogue.api.TheMovieDBApi
import com.gunalirezqi.moviecatalogue.model.TvShowResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TvShowPresenter(
    private val view: TvShowFragment,
    private val apiRepository: ApiRepository,
    private val gson: Gson
) {
    fun getTvShowList() {
        view.showLoading()

        GlobalScope.launch(Dispatchers.Main) {
            val language = view.getString(R.string.language)
            val data = gson.fromJson(
                apiRepository
                    .doRequest(TheMovieDBApi.getTvShow(language)),
                TvShowResponse::class.java
            )

            view.hideLoading()
            if (data.results.isEmpty()) {
                view.handleNotFound()
            } else {
                view.showTvShowList(data.results)
            }
        }
    }
}