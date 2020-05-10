package com.gunalirezqi.moviecatalogue.feature.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.gunalirezqi.moviecatalogue.R
import com.gunalirezqi.moviecatalogue.adapter.MovieAdapter
import com.gunalirezqi.moviecatalogue.api.ApiRepository
import com.gunalirezqi.moviecatalogue.feature.detail.movie.DetailMovieActivity
import com.gunalirezqi.moviecatalogue.model.Movie
import com.gunalirezqi.moviecatalogue.utils.invisible
import com.gunalirezqi.moviecatalogue.utils.visible
import kotlinx.android.synthetic.main.fragment_movie.*
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity

class MovieFragment : Fragment(), MovieView {

    private lateinit var presenter: MoviePresenter
    private lateinit var adapter: MovieAdapter

    private var movie: MutableList<Movie> = mutableListOf()

    companion object {
        private const val STATE_MOVIE = "state_movie"
    }

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
        adapter = MovieAdapter(movie) {
            startActivity<DetailMovieActivity>(
                DetailMovieActivity.EXTRA_MOVIE_ID to it.movieId,
                DetailMovieActivity.EXTRA_MOVIE_TITLE to it.movieTitle
            )
        }
        list_movie.adapter = adapter

        if (savedInstanceState == null) {
            val request = ApiRepository()
            val gson = Gson()

            // Presenter
            presenter = MoviePresenter(this@MovieFragment, request, gson)
            presenter.getMovieList()
        } else {
            val data = savedInstanceState.getParcelableArrayList<Movie>(STATE_MOVIE)
            if (data != null) showMovieList(data)
        }

        swipeRefresh.onRefresh {
            presenter.getMovieList()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(STATE_MOVIE, ArrayList(movie))
    }

    fun handleNotFound() {
        message_movie.text = getString(R.string.empty_movies)
    }

    override fun showLoading() {
        progressbar_movie.isIndeterminate = true
        progressbar_movie.visible()
    }

    override fun hideLoading() {
        progressbar_movie.invisible()
    }

    override fun showMovieList(data: List<Movie>) {
        swipeRefresh.isRefreshing = false
        movie.clear()
        movie.addAll(data)
        adapter.notifyDataSetChanged()
    }
}
