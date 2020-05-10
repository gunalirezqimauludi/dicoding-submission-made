package com.gunalirezqi.moviecatalogue.helper

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.gunalirezqi.moviecatalogue.model.FavoriteMovie
import org.jetbrains.anko.db.*

class DBHelperMovie(ctx: Context) :
    ManagedSQLiteOpenHelper(ctx, "FavoriteMovie.db", null, 1) {

    companion object {
        private const val TABLE_NAME = FavoriteMovie.TABLE_FAVORITE
        private const val ID = FavoriteMovie.ID

        private var instance: DBHelperMovie? = null

        @Synchronized
        fun getInstance(ctx: Context): DBHelperMovie {
            if (instance == null) {
                instance = DBHelperMovie(ctx.applicationContext)
            }
            return instance as DBHelperMovie
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable(
            FavoriteMovie.TABLE_FAVORITE, true,
            FavoriteMovie.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            FavoriteMovie.MOVIE_ID to TEXT + UNIQUE,
            FavoriteMovie.MOVIE_TITLE to TEXT,
            FavoriteMovie.MOVIE_OVERVIEW to TEXT,
            FavoriteMovie.MOVIE_RELEASE_DATE to TEXT,
            FavoriteMovie.MOVIE_VOTE_AVERAGE to TEXT,
            FavoriteMovie.MOVIE_POSTER_PATH to TEXT,
            FavoriteMovie.MOVIE_BACKDROP_PATH to TEXT,
            FavoriteMovie.MOVIE_GENRE to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(FavoriteMovie.TABLE_FAVORITE, true)
    }

    fun queryAll(): Cursor {
        val database = writableDatabase
        return database.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            "$ID DESC",
            null)
    }

    fun queryById(id: String): Cursor {
        val database = writableDatabase
        return database.query(TABLE_NAME, null, "$ID = ?", arrayOf(id), null, null, null, null)
    }

    fun deleteById(id: String): Int {
        val database = writableDatabase
        return database.delete(TABLE_NAME, "$ID = '$id'", null)
    }
}

// Access property for Context
val Context.databaseMovie: DBHelperMovie
    get() = DBHelperMovie.getInstance(applicationContext)