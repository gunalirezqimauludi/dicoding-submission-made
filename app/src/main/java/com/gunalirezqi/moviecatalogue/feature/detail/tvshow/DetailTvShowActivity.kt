package com.gunalirezqi.moviecatalogue.feature.detail.tvshow

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
import com.gunalirezqi.moviecatalogue.helper.databaseTvShow
import com.gunalirezqi.moviecatalogue.model.Credit
import com.gunalirezqi.moviecatalogue.model.DetailTvShow
import com.gunalirezqi.moviecatalogue.model.FavoriteMovie
import com.gunalirezqi.moviecatalogue.model.FavoriteTvShow
import com.gunalirezqi.moviecatalogue.utils.invisible
import com.gunalirezqi.moviecatalogue.utils.visible
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select

class DetailTvShowActivity : AppCompatActivity(), DetailTvShowView {

    private lateinit var presenter: DetailTvShowPresenter
    private lateinit var data: DetailTvShow

    private lateinit var tvShowId: String
    private lateinit var tvShowName: String

    private var tvShowCredit: ArrayList<Credit> = ArrayList()

    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false

    companion object {
        const val EXTRA_TVSHOW_ID = "extra_tv_show_id"
        const val EXTRA_TVSHOW_NAME = "extra_tv_show_name"
        private const val STATE_TVSHOW = "state_tv_show"
        private const val STATE_TVSHOW_CREDIT = "state_tv_show_credit"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Get Intent Content Value
        tvShowId = intent.getStringExtra(EXTRA_TVSHOW_ID)
        tvShowName = intent.getStringExtra(EXTRA_TVSHOW_NAME)

        favoriteState()

        // ActionBar Config
        supportActionBar?.title = tvShowName
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Presenter
        val request = ApiRepository()
        val gson = Gson()
        presenter =
            DetailTvShowPresenter(
                this,
                request,
                gson
            )

        if (savedInstanceState == null) {
            presenter.getTvShowDetail(tvShowId)
            presenter.getTvShowCredit(tvShowId)
        } else {
            val dataDetail = savedInstanceState.getParcelable<DetailTvShow>(STATE_TVSHOW)
            dataDetail?.let { showTvShowDetail(it) }

            val dataCredit = savedInstanceState.getParcelableArrayList<Credit>(
                STATE_TVSHOW_CREDIT
            )
            dataCredit?.let { showTvShowCredit(it) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(STATE_TVSHOW, data)
        outState.putParcelableArrayList(STATE_TVSHOW_CREDIT, tvShowCredit)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu

        setFavoriteMovie()

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_to_favorite -> {
                if (isFavorite) removeFromFavorite() else addToFavorite()

                isFavorite = !isFavorite
                setFavoriteMovie()

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
            databaseTvShow.use {
                insert(
                    FavoriteTvShow.TABLE_FAVORITE,
                    FavoriteTvShow.TVSHOW_ID to data.tvShowId,
                    FavoriteTvShow.TVSHOW_NAME to data.tvShowName,
                    FavoriteTvShow.TVSHOW_OVERVIEW to data.tvShowOverview,
                    FavoriteTvShow.TVSHOW_FIRST_AIR_DATE to data.tvShowFirstAirDate,
                    FavoriteTvShow.TVSHOW_VOTE_AVERAGE to data.tvShowVoteAverage,
                    FavoriteTvShow.TVSHOW_POSTER_PATH to data.tvShowPoster,
                    FavoriteTvShow.TVSHOW_BACKDROP_PATH to data.tvShowBackdrop,
                    FavoriteTvShow.TVSHOW_GENRE to data.tvShowGenre[0].genreName
                )
            }

            Toast.makeText(this, resources.getString(R.string.favorite_add), Toast.LENGTH_SHORT)
                .show()
        } catch (e: SQLiteConstraintException) {
            Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeFromFavorite() {
        try {
            databaseTvShow.use {
                delete(
                    FavoriteMovie.TABLE_FAVORITE, "(TVSHOW_ID = {tvShowId})",
                    "tvShowId" to tvShowId
                )
            }

            Toast.makeText(this, resources.getString(R.string.favorite_remove), Toast.LENGTH_SHORT)
                .show()
        } catch (e: SQLiteConstraintException) {
            Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setFavoriteMovie() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_added_to_favorites)
        else
            menuItem?.getItem(0)?.icon =
                ContextCompat.getDrawable(this, R.drawable.ic_add_to_favorites)
    }

    private fun favoriteState() {
        databaseTvShow.use {
            val result = select(FavoriteTvShow.TABLE_FAVORITE)
                .whereArgs(
                    "(TVSHOW_ID = {tvShowId})",
                    "tvShowId" to tvShowId
                )
            val favorite = result.parseList(classParser<FavoriteMovie>())

            isFavorite = favorite.isNotEmpty()
        }
    }

    override fun showLoading() {
        progressbar_detail.visible()
    }

    override fun hideLoading() {
        progressbar_detail.invisible()
    }

    override fun showTvShowDetail(data: DetailTvShow) {
        this.data = data

        // Set Backdrop
        Picasso.get().load("https://image.tmdb.org/t/p/original/${data.tvShowBackdrop}")
            .fit()
            .centerCrop()
            .into(detail_backdrop)

        // Set Poster
        detail_poster.clipToOutline = true
        Picasso.get().load("https://image.tmdb.org/t/p/original/${data.tvShowPoster}")
            .into(detail_poster)

        // Set Title
        detail_title.text = data.tvShowName

        // Set Release Date
        if (data.tvShowFirstAirDate.isNullOrEmpty()) {
            detail_release_date.text = resources.getString(R.string.empty_release_date)
        } else {
            detail_release_date.text = data.tvShowFirstAirDate?.substring(0, 4)
        }

        // Set Genre
        detail_genre.text = data.tvShowGenre[0].genreName

        // Set RatingBar
        detail_vote_average.text = data.tvShowVoteAverage
        ratingbar_vote_average.rating = (data.tvShowVoteAverage?.toFloat()?.div(10)?.times(5)) ?: 0.0F

        // Set Overview
        if (data.tvShowOverview.isNullOrBlank()) {
            detail_overview.text = resources.getString(R.string.empty_overview)
        } else {
            detail_overview.text = data.tvShowOverview
        }
    }

    override fun showTvShowCredit(data: ArrayList<Credit>) {
        if (data.size > 0) {
            // Set Value
            tvShowCredit = data

            detail_crew_name.text = data[0].crewName
            detail_crew_job.text = data[0].crewJob
        } else {
            detail_crew_name.text = ""
            detail_crew_job.text = ""
        }
    }
}
