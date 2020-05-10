package com.gunalirezqi.moviecatalogue.feature.detail.tvshow

import com.gunalirezqi.moviecatalogue.model.Credit
import com.gunalirezqi.moviecatalogue.model.DetailTvShow

interface DetailTvShowView {
    fun showLoading()
    fun hideLoading()
    fun showTvShowDetail(data: DetailTvShow)
    fun showTvShowCredit(data: ArrayList<Credit>)
}