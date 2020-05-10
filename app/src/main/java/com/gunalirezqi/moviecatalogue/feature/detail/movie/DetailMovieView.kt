package com.gunalirezqi.moviecatalogue.feature.detail.movie

import com.gunalirezqi.moviecatalogue.model.Credit
import com.gunalirezqi.moviecatalogue.model.DetailMovie

interface DetailMovieView {
    fun showLoading()
    fun hideLoading()
    fun showMovieDetail(data: DetailMovie)
    fun showMovieCredit(data: ArrayList<Credit>)
}