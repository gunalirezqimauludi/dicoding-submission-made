package com.gunalirezqi.moviecatalogue.feature.search

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.gunalirezqi.moviecatalogue.R
import com.gunalirezqi.moviecatalogue.adapter.MovieAdapter
import com.gunalirezqi.moviecatalogue.adapter.TvShowAdapter
import com.gunalirezqi.moviecatalogue.api.ApiRepository
import com.gunalirezqi.moviecatalogue.feature.detail.movie.DetailMovieActivity
import com.gunalirezqi.moviecatalogue.feature.detail.tvshow.DetailTvShowActivity
import com.gunalirezqi.moviecatalogue.model.Movie
import com.gunalirezqi.moviecatalogue.model.TvShow
import com.gunalirezqi.moviecatalogue.utils.invisible
import com.gunalirezqi.moviecatalogue.utils.visible
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment_movie.swipeRefresh
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.onRefresh

class SearchActivity : AppCompatActivity(), SearchView {

    private lateinit var presenter: SearchPresenter
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var tvShowAdapter: TvShowAdapter

    private lateinit var query: String
    private var type = "movie"

    private var movie: MutableList<Movie> = mutableListOf()
    private var tvShow: MutableList<TvShow> = mutableListOf()

    companion object {
        private const val STATE_TYPE = "state_type"
        private const val STATE_SEARCH_MOVIES = "state_search_movies"
        private const val STATE_SEARCH_TV_SHOWS = "state_search_tv_shows"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // Get Intent Search Value
        if (Intent.ACTION_SEARCH == intent.action) {
            query = intent.getStringExtra(SearchManager.QUERY)
        }

        // Get Saved Instance State
        if (savedInstanceState != null) {
            type = savedInstanceState.getString(STATE_TYPE, "movie")
        }

        // ActionBar Config
        supportActionBar?.title = query
        supportActionBar?.elevation = 0F

        // Get Content By Type
        if (type == "movie") {
            contentMovies(savedInstanceState)
        } else {
            contentTvShows(savedInstanceState)
        }

        // Trigger Click Button Types
        btn_filter_movies.onClick {
            btn_filter_tv_shows.background = getDrawable(R.drawable.btn_filter)
            type = "movie"

            contentMovies(savedInstanceState)
        }

        btn_filter_tv_shows.onClick {
            btn_filter_movies.background = getDrawable(R.drawable.btn_filter)
            type = "tv_shows"

            contentTvShows(savedInstanceState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_TYPE, type)
        outState.putParcelableArrayList(STATE_SEARCH_MOVIES, ArrayList(movie))
        outState.putParcelableArrayList(STATE_SEARCH_TV_SHOWS, ArrayList(tvShow))
    }

    private fun contentMovies(savedInstanceState: Bundle?) {

        // Set Button Movies to Active
        btn_filter_movies.background = getDrawable(R.drawable.btn_filter_active)

        // Adapter
        list_search.layoutManager = LinearLayoutManager(applicationContext)
        movieAdapter = MovieAdapter(movie) {
            startActivity<DetailMovieActivity>(
                DetailMovieActivity.EXTRA_MOVIE_ID to it.movieId,
                DetailMovieActivity.EXTRA_MOVIE_TITLE to it.movieTitle
            )
        }
        list_search.adapter = movieAdapter

        val request = ApiRepository()
        val gson = Gson()

        if (savedInstanceState == null) {
            presenter = SearchPresenter(this@SearchActivity, request, gson)
            presenter.getMovieList(query)
        } else {
            val data = savedInstanceState.getParcelableArrayList<Movie>(STATE_SEARCH_MOVIES)
            if (data != null) showMovieList(data)
        }

        swipeRefresh.onRefresh {
            presenter = SearchPresenter(this@SearchActivity, request, gson)
            presenter.getMovieList(query)
        }
    }

    private fun contentTvShows(savedInstanceState: Bundle?) {

        // Set Button TV Shows to Active
        btn_filter_tv_shows.background = getDrawable(R.drawable.btn_filter_active)

        // Adapter
        list_search.layoutManager = LinearLayoutManager(applicationContext)
        tvShowAdapter = TvShowAdapter(tvShow) {
            startActivity<DetailTvShowActivity>(
                DetailTvShowActivity.EXTRA_TVSHOW_ID to it.tvShowId,
                DetailTvShowActivity.EXTRA_TVSHOW_NAME to it.tvShowName
            )
        }
        list_search.adapter = tvShowAdapter

        val request = ApiRepository()
        val gson = Gson()

        if (savedInstanceState == null) {
            presenter = SearchPresenter(this@SearchActivity, request, gson)
            presenter.getTvShowList(query)
        } else {
            val data = savedInstanceState.getParcelableArrayList<TvShow>(STATE_SEARCH_TV_SHOWS)
            if (data != null) showTvShowList(data)
        }

        swipeRefresh.onRefresh {
            presenter = SearchPresenter(this@SearchActivity, request, gson)
            presenter.getTvShowList(query)
        }
    }

    fun handleNotFound(status: Boolean, type: String) {
        if (status) {
            if (type == "movies") {
                message_search.text = getString(R.string.empty_movies)
            } else {
                message_search.text = getString(R.string.empty_tv_shows)
            }
        } else {
            message_search.text = ""
        }
    }

    override fun showLoading() {
        progressbar_search.isIndeterminate = true
        progressbar_search.visible()
    }

    override fun hideLoading() {
        progressbar_search.invisible()
    }

    override fun showMovieList(data: List<Movie>) {
        swipeRefresh.isRefreshing = false
        movie.clear()
        movie.addAll(data)
        movieAdapter.notifyDataSetChanged()
    }

    override fun showTvShowList(data: List<TvShow>) {
        swipeRefresh.isRefreshing = false
        tvShow.clear()
        tvShow.addAll(data)
        tvShowAdapter.notifyDataSetChanged()
    }
}
