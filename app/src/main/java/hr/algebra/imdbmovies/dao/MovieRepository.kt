package hr.algebra.imdbmovies.dao

import android.content.ContentValues
import android.database.Cursor

interface MovieRepository {

    fun delete(selection: String?, selectionArgs: Array<String>?): Int

    fun insert(uri: ContentValues?): Long

    fun query(
        projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor

    fun update(
        values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int
}