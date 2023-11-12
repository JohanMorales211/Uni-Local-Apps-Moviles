package com.example.unilocal.activities

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.unilocal.R
import com.example.unilocal.databinding.ActivityDetailPlaceBinding
import com.example.unilocal.db.*
import com.example.unilocal.fragment.CommentsPlaceFragment
import com.example.unilocal.model.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetailPlaceActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var binding:ActivityDetailPlaceBinding
    var codePlace:String = ""
    var place:Place? = null
    lateinit var fragment:Fragment

    private lateinit var mapView: MapView
    private lateinit var google_map: GoogleMap
    private var tienePermiso = false
    private lateinit var placeLocation: LatLng


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_place)

        binding = ActivityDetailPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        codePlace = intent.extras!!.getString("code").toString()

        Firebase.firestore
            .collection("places")
            .document(codePlace)
            .get()
            .addOnSuccessListener {
                var placeF = it.toObject(Place::class.java)
                if (placeF != null) {

                    placeF!!.key = it.id

                    Firebase.firestore
                        .collection("users")
                        .document(placeF.idOwner)
                        .get()
                        .addOnSuccessListener {u->
                            var userF = u.toObject(User::class.java)
                            if (userF != null) {
                                binding.ownerName.text = userF.name
                                binding.placeName.text = placeF!!.name
                                binding.placeDescription.text = placeF!!.description
                                binding.placeDirection.text = placeF!!.direction
                                Glide.with(this)
                                    .load(userF.imgUrl)
                                    .into(binding.userImage  )

                            }
                        }



                    var city = ""//Cities.findByID(placeF!!.idCity)
                    Firebase.firestore
                        .collection("cities")
                        .get()
                        .addOnSuccessListener {
                            for(doc in it){
                                val cityF = doc.toObject(City::class.java)
                                if(cityF.id == placeF.idCity){
                                    city = cityF.name
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            Snackbar.make(binding.root, "$e.message", Snackbar.LENGTH_LONG).show()
                        }
                    var department = ""
                    Firebase.firestore
                        .collection("departments")
                        .get()
                        .addOnSuccessListener {
                            for(doc in it){
                                val depF = doc.toObject(Department::class.java)
                                if(depF.id == placeF.idDepartment){
                                    department = depF.name
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            Snackbar.make(binding.root, "$e.message", Snackbar.LENGTH_LONG).show()
                        }
                    var country = ""
                    Firebase.firestore
                        .collection("countries")
                        .get()
                        .addOnSuccessListener {
                            for(doc in it){
                                val couF = doc.toObject(Country::class.java)
                                if(couF.id == placeF.idCountry){
                                    country = couF.name
                                }
                            }
                        }
                        .addOnFailureListener { e ->
                            Snackbar.make(binding.root, "$e.message", Snackbar.LENGTH_LONG).show()
                        }

                    binding.placeLocation.text = "$city, $department - $country"

                    val statusPlace = placeF!!.isOpen()
                    if(statusPlace == "Open"){
                        binding.placeSchedule.setTextColor(Color.GREEN)
                        binding.placeSchedule.text = getString(R.string.detail_place_open)
                    } else {
                        binding.placeSchedule.setTextColor(Color.RED)
                        binding.placeSchedule.text = getString(R.string.detail_place_closed)
                    }

                    var phoneNumbers = ""

                    if(placeF!!.phoneNumbers.isNotEmpty()){
                        for (tel in placeF!!.phoneNumbers) {
                            phoneNumbers += "$tel, "
                        }
                        phoneNumbers = phoneNumbers.substring(0, phoneNumbers.length - 2)
                    }

                    binding.placePhoneNumbers.text = phoneNumbers

                    var schedules = ""

                    for (schedule in placeF!!.schedules){
                        for (day in schedule.weekDay){
                            schedules += "$day: ${schedule.startTime}:00 - ${schedule.closingTime}:00\n"
                        }
                    }

                    binding.placeSchedules.text = schedules
                    //binding.placeRating.text = placeF!!.getRatingAverage(Comments.listById(placeF!!.id)).toString()

                    fragment= CommentsPlaceFragment.newInstance(codePlace)

                    binding.comments.setOnClickListener {
                        if(fragment.isVisible){
                            hideFragment()
                        } else {
                            showFragment()
                        }
                    }


                    placeLocation = LatLng(placeF!!.latitude, placeF!!.longitude)

                    Firebase.firestore
                        .collection("places")
                        .document(placeF.key)
                        .get()
                        .addOnSuccessListener {
                            Glide.with( this )
                                .load(placeF.images[0])
                                .into(binding.placeImage )
                        }



                }


            }
            .addOnFailureListener {
                Log.e("PLACE DETAIL", "${it.message}")
            }
        mapView = findViewById(R.id.place_map_location)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)




    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }


    private fun showFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.comments_view, fragment)
        fragmentTransaction.commit()
    }

    private fun hideFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.remove(fragment)
        fragmentTransaction.commit()
    }

    companion object {



    }

    override fun onMapReady(googleMap: GoogleMap) {
        codePlace = intent.extras!!.getString("code").toString()
        Firebase.firestore
            .collection("places")
            .document(codePlace)
            .get()
            .addOnSuccessListener {
                var placeF = it.toObject(Place::class.java)
                if (placeF != null) {

                    placeF!!.key = it.id
                    var ownerName = ""

                    placeLocation = LatLng(placeF!!.latitude, placeF!!.longitude)
                    google_map = googleMap
                    google_map.moveCamera( CameraUpdateFactory.newLatLngZoom(placeLocation, 17f))
                    google_map.addMarker(MarkerOptions().position(placeLocation).title(getString(R.string.right_here)))

                    google_map.uiSettings.isZoomControlsEnabled = true
                }
            }


    }
}