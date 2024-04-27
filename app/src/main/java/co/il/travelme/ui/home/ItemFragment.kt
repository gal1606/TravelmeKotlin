package co.il.travelme.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.il.travelme.R
import co.il.travelme.models.Trip
import co.il.travelme.viewmodels.TripVM

/**
 * A fragment representing a list of Items.
 */
class ItemFragment : Fragment() {
    private lateinit var viewModel: TripVM
    private lateinit var adapter: MyItemRecyclerViewAdapter  // הנחה שיש לך אדפטר כלשהו לנתונים

    private var columnCount = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_item_list, container, false)
        viewModel = ViewModelProvider(this).get(TripVM::class.java)
        adapter = MyItemRecyclerViewAdapter()

        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = this@ItemFragment.adapter
            }
        }

        viewModel.trips.observe(viewLifecycleOwner, Observer { trips ->
            Log.d("Fragment", "Trips loaded: ${trips.size}")
            adapter.submitList(trips)
        })



//        // Set the adapter
//        if (view is RecyclerView) {
//            with(view) {
//                layoutManager = when {
//                    columnCount <= 1 -> LinearLayoutManager(context)
//                    else -> GridLayoutManager(context, columnCount)
//                }
//                adapter = MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS)
//            }
//        }
        return view
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            ItemFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}