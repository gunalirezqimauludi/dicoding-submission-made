package com.gunalirezqi.moviecatalogue.feature.favorite.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gunalirezqi.moviecatalogue.R
import com.gunalirezqi.moviecatalogue.adapter.FavoriteMovieAdapter
import com.gunalirezqi.moviecatalogue.feature.detail.movie.DetailMovieActivity
import com.gunalirezqi.moviecatalogue.helper.databaseMovie
import com.gunalirezqi.moviecatalogue.model.FavoriteMovie
import kotlinx.android.synthetic.main.fragment_movie.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity

class MovieFragment : Fragment() {

    private var favorites: MutableList<FavoriteMovie> = mutableListOf()
    private lateinit var adapter: FavoriteMovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adapter
        list_movie.layoutManager = LinearLayoutManager(requireContext())
        adapter = FavoriteMovieAdapter(favorites) {
            startActivity<DetailMovieActivity>(
                DetailMovieActivity.EXTRA_MOVIE_ID to it.movieId,
                DetailMovieActivity.EXTRA_MOVIE_TITLE to it.movieTitle
            )
        }

        list_movie.adapter = adapter
        swipeRefresh.onRefresh {
            showFavorite()
        }

        showFavorite()
    }

    override fun onResume() {
        super.onResume()
        showFavorite()
    }

    private fun showFavorite() {
        favorites.clear()
        context?.databaseMovie?.use {
            swipeRefresh.isRefreshing = false
            val result = select(FavoriteMovie.TABLE_FAVORITE)
            val favorite = result.parseList(classParser<FavoriteMovie>())
            favorites.addAll(favorite)
            adapter.notifyDataSetChanged()
        }

        message_movie.text =
            if (favorites.isEmpty()) resources.getString(R.string.empty_favorites_movies) else ""
    }
}
