package hr.algebra.imdbmovies.api

import android.content.ContentValues
import android.content.Context
import android.util.Log
import hr.algebra.imdbmovies.MOVIE_PROVIDER_CONTENT_URI

import hr.algebra.imdbmovies.MovieReceiver
import hr.algebra.imdbmovies.framework.sendBroadcast
import hr.algebra.imdbmovies.handler.downloadImageAndStore
import hr.algebra.imdbmovies.model.Item
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieFetcher(private val context: Context) {
    private var movieApi: MovieApi
    init{
        val retrofit= Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        movieApi=retrofit.create(MovieApi::class.java)
    }

    fun fetchItems(count: Int){
        val request= movieApi.fetchItems(count)
        request.enqueue(object : Callback<MovieItemResult> {
            override fun onResponse(
                call: Call<MovieItemResult>,
                response: Response<MovieItemResult>
            ) {
                if (response.isSuccessful) {
                    val movieItems = response.body()
                    if (movieItems != null) {
                        populateItems(movieItems.items)
                    } else {
                        Log.e(javaClass.name, "Empty response body")
                    }
                } else {
                    Log.e(
                        javaClass.name,
                        "Failed to fetch movie items. Response code: ${response.code()}"
                    )
                }
            }

            override fun onFailure(call: Call<MovieItemResult>, t: Throwable) {
                Log.e(javaClass.name, "Failed to fetch movie items", t)
            }
        })


    }

    private fun populateItems(movieItems: List<MovieItem>) {
        GlobalScope.launch {
          movieItems.forEach {
              var picturePath= downloadImageAndStore(context,it.image)

              val imagePath = picturePath ?: "default_image_path.jpg"
              val values= ContentValues().apply {
                  put(Item::rank.name,it.rank)
                  put(Item::title.name,it.title)
                  put(Item::fullTitle.name,it.fullTitle)
                  put(Item::year.name,it.year)
                  put(Item::crew.name,it.crew)
                  put(Item::imDbRating.name,it.imDbRating)
                  put(Item::image.name,imagePath)
                  put(Item::read.name,false)

              }
              val uri = context.contentResolver.insert(MOVIE_PROVIDER_CONTENT_URI, values)
              if (uri == null) {
                  Log.e(javaClass.name, "Failed to insert item: ${it.title}")
              }
            /*  context.contentResolver.insert(MOVIE_PROVIDER_CONTENT_URI,values)*/
          }
        }
        context.sendBroadcast<MovieReceiver>()
    }


}