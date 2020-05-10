package com.gunalirezqi.moviecatalogue.helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.gunalirezqi.moviecatalogue.model.FavoriteTvShow
import org.jetbrains.anko.db.*

class DBHelperTvShow(ctx: Context) :
    ManagedSQLiteOpenHelper(ctx, "FavoriteTvShow.db", null, 1) {
    companion object {
        private var instance: DBHelperTvShow? = null

        @Synchronized
        fun getInstance(ctx: Context): DBHelperTvShow {
            if (instance == null) {
                instance = DBHelperTvShow(ctx.applicationContext)
            }
            return instance as DBHelperTvShow
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable(
            FavoriteTvShow.TABLE_FAVORITE, true,
            FavoriteTvShow.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            FavoriteTvShow.TVSHOW_ID to TEXT + UNIQUE,
            FavoriteTvShow.TVSHOW_NAME to TEXT,
            FavoriteTvShow.TVSHOW_OVERVIEW to TEXT,
            FavoriteTvShow.TVSHOW_FIRST_AIR_DATE to TEXT,
            FavoriteTvShow.TVSHOW_VOTE_AVERAGE to TEXT,
            FavoriteTvShow.TVSHOW_POSTER_PATH to TEXT,
            FavoriteTvShow.TVSHOW_BACKDROP_PATH to TEXT,
            FavoriteTvShow.TVSHOW_GENRE to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(FavoriteTvShow.TABLE_FAVORITE, true)
    }
}

// Access property for Context
val Context.databaseTvShow: DBHelperTvShow
    get() = DBHelperTvShow.getInstance(applicationContext)