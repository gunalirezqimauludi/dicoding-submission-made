package com.gunalirezqi.moviecatalogue.feature.detail.tvshow

import com.google.gson.Gson
import com.gunalirezqi.moviecatalogue.R
import com.gunalirezqi.moviecatalogue.api.ApiRepository
import com.gunalirezqi.moviecatalogue.api.TheMovieDBApi
import com.gunalirezqi.moviecatalogue.model.Credit
import com.gunalirezqi.moviecatalogue.model.CreditResponse
import com.gunalirezqi.moviecatalogue.model.DetailTvShow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DetailTvShowPresenter(
    private val view: DetailTvShowActivity,
    private val apiRepository: ApiRepository,
    private val gson: Gson
) {
    fun getTvShowDetail(contentId: String?) {
        view.showLoading()

        GlobalScope.launch(Dispatchers.Main) {
            val language = view.getString(R.string.language)
            val data = gson.fromJson(
                apiRepository.doRequest(TheMovieDBApi.getTvShowDetails(contentId, language)),
                DetailTvShow::class.java
            )

            view.hideLoading()
            view.showTvShowDetail(data)
        }
    }

    fun getTvShowCredit(contentId: String?) {
        view.showLoading()

        GlobalScope.launch(Dispatchers.Main) {
            val language = view.getString(R.string.language)
            val data = gson.fromJson(
                apiRepository.doRequest(TheMovieDBApi.getTvShowCredits(contentId, language)),
                CreditResponse::class.java
            )

            view.hideLoading()
            view.showTvShowCredit(data.crew as ArrayList<Credit>)
        }
    }
}