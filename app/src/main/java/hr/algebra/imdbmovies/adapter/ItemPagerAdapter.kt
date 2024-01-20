package hr.algebra.imdbmovies.adapter

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.imdbmovies.ItemPagerActivity
import hr.algebra.imdbmovies.MOVIE_PROVIDER_CONTENT_URI
import hr.algebra.imdbmovies.POSITION
import hr.algebra.imdbmovies.R
import hr.algebra.imdbmovies.model.Item
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class ItemPagerAdapter(private val context: Context, private val items:MutableList<Item>)
    :RecyclerView.Adapter<ItemPagerAdapter.ViewHolder>()
{
    interface OnRatingSelectedListener {
        fun onRatingSelected(item: Item, rating: Double)

    }
    private var ratingSelectedListener: OnRatingSelectedListener? = null

    fun setOnRatingSelectedListener(listener: OnRatingSelectedListener) {
        ratingSelectedListener = listener
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        private val ivItem = itemView.findViewById<ImageView>(R.id.ivItem)
       val ivRead = itemView.findViewById<ImageView>(R.id.ivRed)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private val tvRank = itemView.findViewById<TextView>(R.id.tvRank)
        private val tvFullTitle = itemView.findViewById<TextView>(R.id.tvFullTitle)
        private val tvYear = itemView.findViewById<TextView>(R.id.tvYear)
        private val tvCrew = itemView.findViewById<TextView>(R.id.tvCrew)
        private val tvImdbrating = itemView.findViewById<TextView>(R.id.tvImdbrating)
        val ratingBar = itemView.findViewById<RatingBar>(R.id.ratingBar)
        var currentRating: Float = 0f
        fun bind(item: Item) {
            Picasso.get()
                .load(File(item.image))
                .error(R.drawable.imdbslika)
                .transform(RoundedCornersTransformation(50, 5))
                .into(ivItem)
            tvTitle.text = item.title
            tvFullTitle.text = item.fullTitle
            tvCrew.text = item.crew
            tvYear.text= item.year.toString()
            tvRank.text=item.rank.toString()
            tvImdbrating.text=item.imDbRating.toString()
            ivRead.setImageResource(if (item.read)R.drawable.green_flag else R.drawable.red_flag)
            ratingBar.rating=item.ratingBar.toFloat()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pager, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.ivRead.setOnClickListener {
            item.read = !item.read
            context.contentResolver.update(
                ContentUris.withAppendedId(MOVIE_PROVIDER_CONTENT_URI,item._id!!),
                ContentValues().apply {
                    put(Item::read.name, item.read)
                },
                null, null
            )
            notifyItemChanged(position)
        }
        holder.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            holder.currentRating = rating
            ratingSelectedListener?.onRatingSelected(item, rating.toDouble())


            item.ratingBar = rating.toDouble()


            val contentValues = ContentValues().apply {
                put(Item::ratingBar.name, item.ratingBar)
            }
            context.contentResolver.update(
                ContentUris.withAppendedId(MOVIE_PROVIDER_CONTENT_URI, item._id!!),
                contentValues,
                null,
                null
            )


            showRatingSavedMessage()
        }


        holder.bind(item)
    }
    private fun showRatingSavedMessage() {
        Toast.makeText(context, "Rating has been successfuly saved", Toast.LENGTH_SHORT).show()
    }

    override fun getItemCount()= items.size
}