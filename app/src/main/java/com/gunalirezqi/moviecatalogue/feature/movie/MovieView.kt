package com.gunalirezqi.moviecatalogue.feature.movie

import com.gunalirezqi.moviecatalogue.model.Movie

interface MovieView {
    fun showLoading()
    fun hideLoading()
    fun showMovieList(data: List<Movie>)
}