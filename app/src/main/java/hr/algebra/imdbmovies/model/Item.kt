package hr.algebra.imdbmovies.model

import com.google.gson.annotations.SerializedName

data class Item(

    var _id:Long?,
    val rank : Int,
    val title : String,
    val fullTitle : String,
    val year : Int,
    val crew : String,
    val imDbRating : Double,
    val image:String,
    var read:Boolean,
    var ratingBar:Double=0.0

)
