package hr.algebra.imdbmovies.api


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

const val API_URL="https://imdb-api.com/en/API/Top250Movies/"

interface MovieApi {
    @GET("k_vbzt0c5m")
    fun fetchItems(@Query("count") count:Int): Call<MovieItemResult>
}