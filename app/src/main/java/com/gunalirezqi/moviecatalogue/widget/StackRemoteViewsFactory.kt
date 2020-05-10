package com.gunalirezqi.moviecatalogue.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.gunalirezqi.moviecatalogue.R
import com.gunalirezqi.moviecatalogue.helper.databaseMovie
import com.gunalirezqi.moviecatalogue.model.FavoriteMovie
import com.squareup.picasso.Picasso
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import java.util.concurrent.ExecutionException

internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private var favoriteMovieItems: ArrayList<FavoriteMovie> = ArrayList()

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        favoriteMovieItems.clear()
        mContext.databaseMovie.use {
            val result = select(FavoriteMovie.TABLE_FAVORITE)
            val favorite = result.parseList(classParser<FavoriteMovie>())
            if (favorite.isNotEmpty()) {
                favoriteMovieItems.addAll(favorite)
            }
        }
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = favoriteMovieItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val remoteViews = RemoteViews(mContext.packageName, R.layout.item_widget)

        if (favoriteMovieItems.isNotEmpty()) {
            val movieTitle = favoriteMovieItems[position].movieTitle
            val movieReleaseDate = favoriteMovieItems[position].movieReleaseDate?.substring(0, 4)
            val movieVoteAverage = favoriteMovieItems[position].movieVoteAverage
            val moviePoster =
                "https://image.tmdb.org/t/p/original/${favoriteMovieItems[position].moviePoster}"

            try {
                val bitmapPoster: Bitmap = Picasso.get().load(moviePoster).get()
                remoteViews.setImageViewBitmap(R.id.widget_movie_poster, bitmapPoster)
            } catch (e: ExecutionException) {
                e.printStackTrace()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            remoteViews.setTextViewText(R.id.widget_movie_title, movieTitle)
            remoteViews.setTextViewText(R.id.widget_movie_release_date, movieReleaseDate)
            remoteViews.setTextViewText(R.id.widget_movie_user_score, movieVoteAverage)
        }

        val movieTitle = if (favoriteMovieItems.isNotEmpty()) favoriteMovieItems[position].movieTitle else ""

        val extras = bundleOf(
            FavoriteMovieWidget.EXTRA_ITEM to position,
            FavoriteMovieWidget.EXTRA_TITLE to movieTitle
        )

        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        remoteViews.setOnClickFillInIntent(R.id.widget_movie_poster, fillInIntent)
        return remoteViews
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}