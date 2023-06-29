package com.example.supfitness

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions



class FootRacesFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_foot_races, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return rootView
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        val pariscampus = LatLng(48.87552854619609, 2.4107236200292896,)
        mMap.addMarker(MarkerOptions().position(pariscampus).title("CAMPUS Estiam Paris"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pariscampus))
    }

}