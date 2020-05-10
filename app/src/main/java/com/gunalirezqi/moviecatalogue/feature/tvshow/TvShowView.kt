package com.gunalirezqi.moviecatalogue.feature.tvshow

import com.gunalirezqi.moviecatalogue.model.TvShow

interface TvShowView {
    fun showLoading()
    fun hideLoading()
    fun showTvShowList(data: List<TvShow>)
}