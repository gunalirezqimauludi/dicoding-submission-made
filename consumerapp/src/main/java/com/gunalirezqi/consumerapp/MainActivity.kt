package com.gunalirezqi.consumerapp

import android.annotation.SuppressLint
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.gunalirezqi.consumerapp.adapter.MovieAdapter
import com.gunalirezqi.consumerapp.helper.MappingHelper
import com.gunalirezqi.consumerapp.model.Movie
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: MovieAdapter

    companion object {
        private const val AUTHORITY = "com.gunalirezqi.moviecatalogue"
        private const val SCHEME = "content"

        private const val EXTRA_STATE = "EXTRA_STATE"

        val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(Movie.TABLE_FAVORITE)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Consumer Movies"

        list_movie.layoutManager = GridLayoutManager(this, 2)
        list_movie.setHasFixedSize(true)

        adapter = MovieAdapter()
        list_movie.adapter = adapter

        if (savedInstanceState == null) {
            loadMoviesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Movie>(EXTRA_STATE)
            if (list != null) {
                adapter.listMovies = list
            }
        }

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadMoviesAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadMoviesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Movie>(EXTRA_STATE)
            if (list != null) adapter.listMovies = list
        }

        swipeRefresh.setOnRefreshListener {
            loadMoviesAsync()
        }
    }

    @SuppressLint("Recycle")
    private fun loadMoviesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressbar_movie.visibility = View.VISIBLE
            val deferredMovies = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null) as Cursor
                 MappingHelper.mapCursorToArrayList(cursor)
            }

            val movies = deferredMovies.await()

            progressbar_movie.visibility = View.INVISIBLE
            swipeRefresh.isRefreshing = false

            if (movies.size > 0) {
                adapter.listMovies = movies
                message_movie.text = ""
            } else {
                adapter.listMovies = ArrayList()
                message_movie.text = resources.getString(R.string.empty_movies)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listMovies)
    }
}
