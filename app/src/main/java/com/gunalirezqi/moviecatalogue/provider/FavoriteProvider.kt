package com.gunalirezqi.moviecatalogue.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.gunalirezqi.moviecatalogue.helper.DBHelperMovie
import com.gunalirezqi.moviecatalogue.model.FavoriteMovie

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class FavoriteProvider : ContentProvider() {

    companion object {
        private const val AUTHORITY = "com.gunalirezqi.moviecatalogue"
        private const val SCHEME = "content"
        private const val TABLE_NAME = FavoriteMovie.TABLE_FAVORITE

        val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build()

        private const val FAVORITE = 1
        private const val FAVORITE_ID = 2
        private lateinit var dbHelperMovie: DBHelperMovie

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            // content://com.gunalirezqi.moviecatalogue/TABLE_FAVORITE
            uriMatcher.addURI(AUTHORITY, TABLE_NAME, FAVORITE)

            // content://com.gunalirezqi.moviecatalogue/TABLE_FAVORITE/id
            uriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", FAVORITE_ID)
        }
    }

    override fun onCreate(): Boolean {
        dbHelperMovie = DBHelperMovie.getInstance(context as Context)
        dbHelperMovie.writableDatabase
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            FAVORITE -> dbHelperMovie.queryAll()
            FAVORITE_ID -> dbHelperMovie.queryById(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? = null

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (FAVORITE_ID) {
            uriMatcher.match(uri) -> dbHelperMovie.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }
}
