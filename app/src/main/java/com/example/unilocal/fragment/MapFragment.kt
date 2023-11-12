package com.example.unilocal.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.unilocal.R
import com.example.unilocal.activities.DetailPlaceActivity
import com.example.unilocal.databinding.FragmentInfoPlaceBinding
import com.example.unilocal.databinding.FragmentMapBinding
import com.example.unilocal.db.*
import com.example.unilocal.model.Place
import com.example.unilocal.model.PlaceStatus
import com.example.unilocal.model.Rol
import com.example.unilocal.model.User
import com.example.unilocal.model.dontUse.Administrator
import com.example.unilocal.ui.login.LoginActivity
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener{

    lateinit var binding:FragmentMapBinding
    private lateinit var google_map: GoogleMap
    private var tienePermiso = false
    private val defaultLocation = LatLng(4.550923, -75.6557201)
    private lateinit var permissionsResultCallback:ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionsResultCallback = registerForActivityResult(
            ActivityResultContracts.RequestPermission()){
            when (it) {
                true -> { println("Permiso aceptado") }
                false -> { println("Permiso denegado") }
            }
        }
        getLocationPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMapBinding.inflate(inflater, container, false)
        val mapFragment = childFragmentManager.findFragmentById( R.id.principal_map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        google_map = googleMap
        google_map.uiSettings.isZoomControlsEnabled = true

        /*var user = User("Joselito", "Gonzales", "eljoselito", "joselito@gmail.com",
            1, 1, 1, 22, "", "4375987564", Rol.MODERATOR)
        Firebase.firestore
            .collection("users")
            .add(user)
            .addOnSuccessListener {
            }

        AQUI SE HACE PARA METER DATOS QUEMADOS

        Administrators.list().forEach {
            Firebase.firestore
                .collection("admins")
                .add(it)
                .addOnSuccessListener {
                }
        }
        }*/



        Firebase.firestore
            .collection("places")
            .whereEqualTo("status", PlaceStatus.ACCEPTED)
            .get()
            .addOnSuccessListener {
                for(doc in it) {
                    var place = doc.toObject(Place::class.java)
                    place.key = doc.id

                    googleMap.addMarker(
                        MarkerOptions().position(LatLng(place.latitude, place.longitude))
                            .title(place.name)
                    )!!.tag = place.key
                }
            }
            .addOnFailureListener {
            }

        try{
            if(tienePermiso){
                google_map.isMyLocationEnabled = true
                google_map.uiSettings.isMyLocationButtonEnabled = true
            } else {
                google_map.isMyLocationEnabled = false
                google_map.uiSettings.isMyLocationButtonEnabled = false
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
        getLocation()
        google_map.setOnInfoWindowClickListener(this)
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            tienePermiso = true
        } else {
            permissionsResultCallback.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun getLocation() {
        try {
            if (tienePermiso) {
                val ubicacionActual =
                    LocationServices.getFusedLocationProviderClient(requireActivity()).lastLocation
                ubicacionActual.addOnCompleteListener(requireActivity()) {
                    if (it.isSuccessful) {
                        val ubicacion = it.result
                        if (ubicacion != null) {
                            val latlng = LatLng(ubicacion.latitude, ubicacion.longitude)
                            google_map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15F))

                        }
                    } else {
                        google_map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation,
                            15F))
                        google_map.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    override fun onInfoWindowClick(marker: Marker) {
        var key:String = marker.tag as String
        Firebase.firestore
            .collection("places")
            .document(key)
            .get()
            .addOnSuccessListener {
                val intent = Intent(context, DetailPlaceActivity::class.java)
                intent.putExtra("code", key)
                startActivity(intent)
            }
    }

}