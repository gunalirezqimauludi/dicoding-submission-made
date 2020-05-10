package com.gunalirezqi.moviecatalogue.feature.search

import com.gunalirezqi.moviecatalogue.model.Movie
import com.gunalirezqi.moviecatalogue.model.TvShow

interface SearchView {
    fun showLoading()
    fun hideLoading()
    fun showMovieList(data: List<Movie>)
    fun showTvShowList(data: List<TvShow>)
}