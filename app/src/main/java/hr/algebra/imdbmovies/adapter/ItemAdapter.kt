package hr.algebra.imdbmovies.adapter

import android.content.ContentUris
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import hr.algebra.imdbmovies.ItemPagerActivity
import hr.algebra.imdbmovies.MOVIE_PROVIDER_CONTENT_URI
import hr.algebra.imdbmovies.POSITION
import hr.algebra.imdbmovies.R
import hr.algebra.imdbmovies.framework.startActivity
import hr.algebra.imdbmovies.model.Item
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation
import java.io.File

class ItemAdapter(private val context: Context, private val items:MutableList<Item>)
    :RecyclerView.Adapter<ItemAdapter.ViewHolder>()
{

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        private val ivItem = itemView.findViewById<ImageView>(R.id.ivItem)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)



        fun bind(item: Item) {
            Picasso.get()
                .load(File(item.image))
                .error(R.drawable.imdbslika)
                .transform(RoundedCornersTransformation(50, 5))
                .into(ivItem)
            tvTitle.text = item.title

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.itemView.setOnLongClickListener {
            deleteItem(position)
            true
        }
        holder.itemView.setOnClickListener {
            context.startActivity<ItemPagerActivity>(POSITION,position)
        }




        holder.bind(item)
    }

    private fun deleteItem(position: Int) {
        val item = items[position]
        context.contentResolver.delete(
            ContentUris.withAppendedId(MOVIE_PROVIDER_CONTENT_URI, item._id!!),
            null,
            null
        )
        File(item.image).delete()
        items.removeAt(position)
        notifyDataSetChanged()
    }

    override fun getItemCount()= items.size


}