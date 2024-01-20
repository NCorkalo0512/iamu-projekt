package hr.algebra.imdbmovies

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import hr.algebra.imdbmovies.dao.MovieRepository
import hr.algebra.imdbmovies.factory.getMovieRepository
import hr.algebra.imdbmovies.model.Item


private const val AUTHORITY = "hr.algebra.imdbmovies.api.provider"
private const val PATH = "items"
val MOVIE_PROVIDER_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH")

private const val ITEMS = 10
private const val ITEM_ID = 20
private val URI_MATCHER= with(UriMatcher(UriMatcher.NO_MATCH)){
   addURI(AUTHORITY, PATH,ITEMS)
    addURI(AUTHORITY, "$PATH/#",ITEM_ID)
    this
}
class MovieProvider : ContentProvider() {

    private lateinit var movieRepository: MovieRepository

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
       when(URI_MATCHER.match(uri)){
           ITEMS->return movieRepository.delete(selection,selectionArgs)
           ITEM_ID->
               uri.lastPathSegment?.let {
                   return movieRepository.delete("${Item::_id.name}=?", arrayOf(it))
               }


       }
        throw IllegalArgumentException("No such uri")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val id=movieRepository.insert(values)
        return ContentUris.withAppendedId(MOVIE_PROVIDER_CONTENT_URI,id)
    }

    override fun onCreate(): Boolean {
       movieRepository = getMovieRepository(context)


        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? = movieRepository.query(
        projection,
        selection,
        selectionArgs,
        sortOrder
    )


    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        when(URI_MATCHER.match(uri)){
            ITEMS->return movieRepository.update(values,selection,selectionArgs)
            ITEM_ID->
                uri.lastPathSegment?.let {
                    return movieRepository.update(values,"${Item::_id.name}=?", arrayOf(it))
                }


        }
        throw IllegalArgumentException("No such uri")
    }
}