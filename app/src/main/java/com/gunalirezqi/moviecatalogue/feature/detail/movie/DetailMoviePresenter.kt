package com.gunalirezqi.moviecatalogue.feature.detail.movie

import com.google.gson.Gson
import com.gunalirezqi.moviecatalogue.R
import com.gunalirezqi.moviecatalogue.api.ApiRepository
import com.gunalirezqi.moviecatalogue.api.TheMovieDBApi
import com.gunalirezqi.moviecatalogue.model.Credit
import com.gunalirezqi.moviecatalogue.model.CreditResponse
import com.gunalirezqi.moviecatalogue.model.DetailMovie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailMoviePresenter(
    private val view: DetailMovieActivity,
    private val apiRepository: ApiRepository,
    private val gson: Gson
) {
    fun getMovieDetail(contentId: String?) {
        view.showLoading()

        GlobalScope.launch(Dispatchers.Main) {
            val language = view.getString(R.string.language)
            val data = gson.fromJson(
                apiRepository.doRequest(TheMovieDBApi.getMovieDetails(contentId, language)),
                DetailMovie::class.java
            )

            view.hideLoading()
            view.showMovieDetail(data)
        }
    }

    fun getMovieCredit(contentId: String?) {
        view.showLoading()

        GlobalScope.launch(Dispatchers.Main) {
            val language = view.getString(R.string.language)
            val data = gson.fromJson(
                apiRepository.doRequest(TheMovieDBApi.getMovieCredits(contentId, language)),
                CreditResponse::class.java
            )

            view.hideLoading()
            view.showMovieCredit(data.crew as ArrayList<Credit>)
        }
    }
}