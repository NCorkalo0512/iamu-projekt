package hr.algebra.imdbmovies.factory

import android.content.Context
import hr.algebra.imdbmovies.dao.MovieSqlHelper

fun getMovieRepository(context: Context?)= MovieSqlHelper(context)