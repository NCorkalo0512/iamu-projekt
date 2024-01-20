package hr.algebra.imdbmovies


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.media3.database.DatabaseProvider
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.imdbmovies.adapter.ItemAdapter
import hr.algebra.imdbmovies.api.MovieFetcher
import hr.algebra.imdbmovies.databinding.ActivityHostBinding
import hr.algebra.imdbmovies.databinding.FragmentMovieItemBinding
import hr.algebra.imdbmovies.framework.fetchItems
import hr.algebra.imdbmovies.model.Item


/**
 * A simple [Fragment] subclass.
 * Use the [MovieItemFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieItemFragment : Fragment() {
    private lateinit var binding: FragmentMovieItemBinding
    private lateinit var items:MutableList<Item>
    private val movieFetcher: MovieFetcher by lazy { MovieFetcher(requireContext()) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       items = requireContext().fetchItems()

        binding = FragmentMovieItemBinding.inflate(inflater, container, false)
        return binding.root
    }
    private fun fetchItemsFromApiAndStore() {
        movieFetcher.fetchItems(count = 100)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchItemsFromApiAndStore()
        binding.rvItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = ItemAdapter(requireContext(), items)
        }

    }




}