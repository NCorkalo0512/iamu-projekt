package hr.algebra.imdbmovies.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import hr.algebra.imdbmovies.model.Item

private const val DB_NAME = "items.db"
private const val DB_VERSION = 2
private const val TABLE_NAME = "items"

class MovieSqlHelper(context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION), MovieRepository {

    override fun onCreate(db: SQLiteDatabase) {
        createTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            alterTableAddColumn(db)
        } else {
            dropTable(db)
            onCreate(db)
        }
    }

    override fun delete(selection: String?, selectionArgs: Array<String>?) =
        writableDatabase.delete(TABLE_NAME, selection, selectionArgs)

    override fun insert(values: ContentValues?) =
        writableDatabase.insert(TABLE_NAME, null, values)

    override fun query(
        projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor = readableDatabase.query(
        TABLE_NAME,
        projection,
        selection,
        selectionArgs,
        null,
        null,
        sortOrder
    )

    override fun update(values: ContentValues?, selection: String?, selectionArgs: Array<String>?) =
        writableDatabase.update(TABLE_NAME, values, selection, selectionArgs)

    private fun createTable(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "${Item::_id.name} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${Item::rank.name} INTEGER NOT NULL, " +
                "${Item::title.name} TEXT NOT NULL, " +
                "${Item::fullTitle.name} TEXT NOT NULL, " +
                "${Item::year.name} INTEGER NOT NULL, " +
                "${Item::crew.name} TEXT NOT NULL, " +
                "${Item::imDbRating.name} DOUBLE NOT NULL, " +
                "${Item::image.name} TEXT NOT NULL, " +
                "${Item::read.name} INTEGER NOT NULL, " +
                "${Item::ratingBar.name} REAL NOT NULL DEFAULT 0.0" +
                ")"
        db.execSQL(createTableQuery)
    }

    private fun alterTableAddColumn(db: SQLiteDatabase) {
        val alterTableQuery = "ALTER TABLE $TABLE_NAME ADD COLUMN ${Item::ratingBar.name} REAL NOT NULL DEFAULT 0.0"
        db.execSQL(alterTableQuery)
    }

    private fun dropTable(db: SQLiteDatabase) {
        val dropTableQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db.execSQL(dropTableQuery)
    }
}