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
import co.il.travelme.StoreTripVM.viewModel
import co.il.travelme.models.Trip
import co.il.travelme.viewmodels.TripVM

/**
 * A fragment representing a list of Items.
 */
class ItemFragment : Fragment() {

    private lateinit var adapter: MyItemRecyclerViewAdapter  // הנחה שיש לך אדפטר כלשהו לנתונים


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

        return view
    }

}