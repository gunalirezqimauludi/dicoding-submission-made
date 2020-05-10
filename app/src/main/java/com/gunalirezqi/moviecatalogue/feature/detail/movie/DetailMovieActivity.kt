package com.gunalirezqi.moviecatalogue.feature.detail.movie

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.gunalirezqi.moviecatalogue.R
import com.gunalirezqi.moviecatalogue.api.ApiRepository
import com.gunalirezqi.moviecatalogue.helper.databaseMovie
import com.gunalirezqi.moviecatalogue.model.Credit
import com.gunalirezqi.moviecatalogue.model.DetailMovie
import com.gunalirezqi.moviecatalogue.model.FavoriteMovie
import com.gunalirezqi.moviecatalogue.utils.invisible
import com.gunalirezqi.moviecatalogue.utils.visible
import com.gunalirezqi.moviecatalogue.widget.FavoriteMovieWidget
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

class DetailMovieActivity : AppCompatActivity(), DetailMovieView {

    private lateinit var presenter: DetailMoviePresenter
    private lateinit var data: DetailMovie

    private lateinit var movieId: String
    private lateinit var movieTitle: String

    private var movieCredit: ArrayList<Credit> = ArrayList()

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    companion object {
        const val EXTRA_MOVIE_ID = "extra_movie_id"
        const val EXTRA_MOVIE_TITLE = "extra_movie_title"
        private const val STATE_MOVIE = "state_movie"
        private const val STATE_MOVIE_CREDIT = "state_movie_credit"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Get Intent Content Value
        movieId = intent.getStringExtra(EXTRA_MOVIE_ID)
        movieTitle = intent.getStringExtra(EXTRA_MOVIE_TITLE)

        favoriteState()

        // ActionBar Config
        supportActionBar?.title = movieTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Presenter
        val request = ApiRepository()
        val gson = Gson()
        presenter =
            DetailMoviePresenter(
                this,
                request,
                gson
            )

        if (savedInstanceState == null) {
            presenter.getMovieDetail(movieId)
            presenter.getMovieCredit(movieId)
        } else {
            val dataDetail = savedInstanceState.getParcelable<DetailMovie>(STATE_MOVIE)
            dataDetail?.let { showMovieDetail(it) }

            val dataCredit = savedInstanceState.getParcelableArrayList<Credit>(
                STATE_MOVIE_CREDIT
            )
            dataCredit?.let { showMovieCredit(it) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(STATE_MOVIE, data)
        outState.putParcelableArrayList(STATE_MOVIE_CREDIT, movieCredit)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu

        setFavorite()

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_to_favorite -> {
                if (isFavorite) removeFromFavorite() else addToFavorite()

                isFavorite = !isFavorite
                setFavorite()

                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun addToFavorite() {
        try {
            databaseMovie.use {
                insert(
                    FavoriteMovie.TABLE_FAVORITE,
                    FavoriteMovie.MOVIE_ID to data.movieId,
                    FavoriteMovie.MOVIE_TITLE to data.movieTitle,
                    FavoriteMovie.MOVIE_OVERVIEW to data.movieOverview,
                    FavoriteMovie.MOVIE_RELEASE_DATE to data.movieReleaseDate,
                    FavoriteMovie.MOVIE_VOTE_AVERAGE to data.movieVoteAverage,
                    FavoriteMovie.MOVIE_POSTER_PATH to data.moviePoster,
                    FavoriteMovie.MOVIE_BACKDROP_PATH to data.movieBackdrop,
                    FavoriteMovie.MOVIE_GENRE to data.movieGenre[0].genreName
                )
            }

            updateFavoriteMoviesStackWidget()

            Toast.makeText(this, resources.getString(R.string.favorite_add), Toast.LENGTH_SHORT)
                .show()
        } catch (e: SQLiteConstraintException) {
            Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeFromFavorite() {
        try {
            databaseMovie.use {
                delete(
                    FavoriteMovie.TABLE_FAVORITE, "(MOVIE_ID = {movieId})",
                    "movieId" to movieId
                )
            }

            updateFavoriteMoviesStackWidget()

            Toast.makeText(this, resources.getString(R.string.favorite_remove), Toast.LENGTH_SHORT)
                .show()
        } catch (e: SQLiteConstraintException) {
            Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_added_to_favorites)
        else
            menuItem?.getItem(0)?.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_add_to_favorites)
    }

    private fun favoriteState() {
        databaseMovie.use {
            val result = select(FavoriteMovie.TABLE_FAVORITE)
                .whereArgs(
                    "(MOVIE_ID = {movieId})",
                    "movieId" to movieId
                )
            val favorite = result.parseList(classParser<FavoriteMovie>())

            isFavorite = favorite.isNotEmpty()
        }
    }

    private fun updateFavoriteMoviesStackWidget() {
        val context: Context = applicationContext
        val widgetManager = AppWidgetManager.getInstance(context)
        val componentName = ComponentName(context, FavoriteMovieWidget::class.java)
        val idAppWidget = widgetManager.getAppWidgetIds(componentName)
        widgetManager.notifyAppWidgetViewDataChanged(idAppWidget, R.id.stack_view)
    }

    override fun showLoading() {
        progressbar_detail.visible()
    }

    override fun hideLoading() {
        progressbar_detail.invisible()
    }

    override fun showMovieDetail(data: DetailMovie) {
        this.data = data

        // Set Backdrop
        Picasso.get().load("https://image.tmdb.org/t/p/original/${data.moviePoster}")
            .fit()
            .centerCrop()
            .into(detail_poster)

        // Set Poster
        detail_poster.clipToOutline = true
        Picasso.get().load("https://image.tmdb.org/t/p/original/${data.movieBackdrop}")
            .into(detail_backdrop)

        // Set Title
        detail_title.text = data.movieTitle

        // Set Release Date
        if (data.movieReleaseDate.isNullOrEmpty()) {
            detail_release_date.text = resources.getString(R.string.empty_release_date)
        } else {
            detail_release_date.text = data.movieReleaseDate?.substring(0, 4)
        }

        // Set Genre
        detail_genre.text = data.movieGenre[0].genreName

        // Set RatingBar
        detail_vote_average.text = data.movieVoteAverage
        ratingbar_vote_average.rating = (data.movieVoteAverage?.toFloat()?.div(10)?.times(5)) ?: 0.0F

        // Set Overview
        if (data.movieOverview.isNullOrBlank()) {
            detail_overview.text = resources.getString(R.string.empty_overview)
        } else {
            detail_overview.text = data.movieOverview
        }
    }

    override fun showMovieCredit(data: ArrayList<Credit>) {
        if (data.size > 0) {
            // Set Value
            movieCredit = data

            detail_crew_name.text = data[0].crewName
            detail_crew_job.text = data[0].crewJob
        } else {
            detail_crew_name.text = ""
            detail_crew_job.text = ""
        }
    }
}
