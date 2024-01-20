package hr.algebra.imdbmovies

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import hr.algebra.imdbmovies.adapter.ItemAdapter
import hr.algebra.imdbmovies.adapter.ItemPagerAdapter
import hr.algebra.imdbmovies.dao.MovieSqlHelper
import hr.algebra.imdbmovies.databinding.ActivityItemPagerBinding
import hr.algebra.imdbmovies.framework.fetchItems
import hr.algebra.imdbmovies.model.Item

const val POSITION= "hr.algebra.imdbmovies.position"
class ItemPagerActivity : AppCompatActivity(), ItemPagerAdapter.OnRatingSelectedListener {

    private lateinit var binding: ActivityItemPagerBinding
    private lateinit var items: MutableList<Item>
    private var itemPosition= 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        items = fetchItems()
        initPager()
        val adapter = ItemPagerAdapter(this, items)
        adapter.setOnRatingSelectedListener(this)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        @Suppress("DEPRECATION")
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    override fun onRatingSelected(item: Item, rating: Double) {
        val dbHelper = MovieSqlHelper(this)
        val ratingValue = rating.toFloat()
        val values = ContentValues().apply {
            put(Item::imDbRating.name, ratingValue)
        }


        val selection = "${Item::_id.name} = ?"
        val selectionArgs = arrayOf(item._id.toString())

        dbHelper.update(values,selection,selectionArgs)

     showRatingDialog(rating)


    }
    private fun showRatingDialog(rating: Double){
        AlertDialog.Builder(this)
            .setTitle("Rating")
            .setMessage("Your rating has been saved: $rating")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    private fun initPager() {
        items= fetchItems()
        itemPosition=intent.getIntExtra(POSITION, itemPosition)
        binding.viewPager.adapter= ItemPagerAdapter(this, items)
        binding.viewPager.currentItem= itemPosition
    }
}