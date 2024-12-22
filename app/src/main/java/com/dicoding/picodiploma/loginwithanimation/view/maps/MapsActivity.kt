package com.dicoding.picodiploma.loginwithanimation.view.maps

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.Result
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMapsBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val viewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var mMap: GoogleMap
    private var _binding: ActivityMapsBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val indonesia = LatLng(-6.2, 106.8)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(indonesia))

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        addStoryMarker()
    }

    private fun addStoryMarker() {
        viewModel.getSession().observe(this) { user ->
            viewModel.getAllStoriesWithLocation(user.token).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            // Tambahkan log untuk debug
                            Log.d("MapsActivity", "Loading data...")
                        }

                        is Result.Success -> {
                            val userStoriesLocation = result.data.listStory
                            userStoriesLocation?.forEach { userStory ->
                                try {
                                    // Gunakan safe call untuk mencegah error
                                    val lat = userStory?.lat ?: 0.0
                                    val lon = userStory?.lon ?: 0.0
                                    val name = userStory?.name ?: "Unknown"
                                    val description = userStory?.description ?: "No Description"

                                    val latLng = LatLng(lat, lon)
                                    mMap.addMarker(
                                        MarkerOptions()
                                            .position(latLng)
                                            .title(name)
                                            .snippet(description)
                                    )
                                } catch (e: Exception) {
                                    Log.e("MapsActivity", "Error adding marker: ${e.message}")
                                }
                            }
                        }

                        is Result.Error -> {
                            Toast.makeText(
                                this,
                                "Terjadi kesalahan: ${result.error}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
