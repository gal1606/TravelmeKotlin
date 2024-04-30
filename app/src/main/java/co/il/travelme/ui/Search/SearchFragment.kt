package co.il.travelme.ui.Search

import LocationViewModel
import android.Manifest
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.il.travelme.CurrentUser
import co.il.travelme.R
import co.il.travelme.StoreTripVM
import co.il.travelme.StoreTripVM.viewModel
import co.il.travelme.databinding.FragmentProfileBinding
import co.il.travelme.databinding.FragmentSearchBinding
import co.il.travelme.models.Trip
import co.il.travelme.ui.home.MyItemRecyclerViewAdapter

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MyItemRecyclerViewAdapter
    private val locationViewModel: LocationViewModel by viewModels()
    private lateinit var googleMap: GoogleMap
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        locationViewModel.handlePermissions(permissions)
    }

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        this.googleMap = googleMap
        googleMap.uiSettings.isMyLocationButtonEnabled = true

        locationViewModel.getLastLocation()

        locationViewModel._locationData.observe(viewLifecycleOwner) { location ->
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        }

        this.googleMap = googleMap
        viewModel.trips.observe(viewLifecycleOwner, Observer { trips ->
            addMarkersToMap(trips, googleMap)
        })

        /*        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))*/
    }



    private fun moveCameraToLocation(location: LatLng?) {
        location?.let {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 15f))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.getRoot()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        //בקשת הרשאות
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
//בניית הLIST של תוצאות החיפוש
        adapter = MyItemRecyclerViewAdapter()

        binding.list.layoutManager = LinearLayoutManager(context)
        binding.list.adapter = adapter

        StoreTripVM.viewModel.filterTrips.observe(viewLifecycleOwner, Observer { trips ->
            Log.i("gil", "trip size: " + trips.size)
            adapter.submitList(trips)
        })
//ביצוע חיפוש
        binding.findTripButton.setOnClickListener {
            StoreTripVM.viewModel.filterTrips(
                binding.spinner.getSelectedItem().toString(),
                binding.editTextText2.text.toString()
            )
        }

    }

// הוספת מרקרים למפה ככמות הטיולים במאגר הנתונים
    private fun addMarkersToMap(trips: List<Trip>, googleMap: GoogleMap) {
        for (trip in trips) {
            val geoPoint = trip.coord  // נניח ש-location הוא מסוג GeoPoint
            val location = LatLng(geoPoint.latitude, geoPoint.longitude)
            googleMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title(trip.level)  // שם הטיול
                    .snippet(trip.description)  // תיאור הטיול
            )
        }
    }
}
