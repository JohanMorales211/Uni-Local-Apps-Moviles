package com.example.unilocal.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.unilocal.R
import com.example.unilocal.activities.DetailPlaceActivity
import com.example.unilocal.activities.SearchResultActivity
import com.example.unilocal.databinding.FragmentImageBinding
import com.example.unilocal.databinding.ItemPlaceBinding
import com.example.unilocal.db.Categories
import com.example.unilocal.db.Comments
import com.example.unilocal.fragment.binding
import com.example.unilocal.model.Category
import com.example.unilocal.model.Place
import com.example.unilocal.model.PlaceStatus
import com.example.unilocal.model.Schedule
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PlaceAdapter(var list:ArrayList<Place>): RecyclerView.Adapter<PlaceAdapter.ViewHolder>(),
    OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var google_map: GoogleMap
    private var tienePermiso = false
    private val defaultLocation = LatLng(4.550923, -75.6557201)
    lateinit var context:Context
    lateinit var viewPager: ViewPager2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)


        mapView = v.findViewById(R.id.place_point)
        mapView.onCreate(null)
        mapView.getMapAsync(this)
        context = parent.context
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(var itemView:View):RecyclerView.ViewHolder(itemView), OnClickListener{

        val name:TextView = itemView.findViewById(R.id.place_name)
        val direction:TextView = itemView.findViewById(R.id.place_direction)
        val startTime:TextView = itemView.findViewById(R.id.start_time)
        val closingTime:TextView = itemView.findViewById(R.id.closing_time)
        val status:TextView = itemView.findViewById(R.id.status_place)
        val image:ImageView = itemView.findViewById(R.id.place_image)
        val icon:TextView = itemView.findViewById(R.id.place_icon)
        val ratingX:TextView = itemView.findViewById(R.id.place_rating)
        var codePlace:String = ""

        init{
            itemView.setOnClickListener(this)
        }

        fun bind(place: Place){
            name.text = place.name
            direction.text = place.direction
            startTime.text = place.schedules[0].startTime.toString() + ":00"
            closingTime.text = place.schedules[0].closingTime.toString() + ":00"

            Glide.with(context)
                .load(place.images[0])
                .into(image )


            val rating = 0//place.getRatingAverage(Comments.listById(place.id))
            val statusPlace = place.isOpen()
            val place_AorR = place.status
            if (place_AorR != null) {
                if(place_AorR.equals(PlaceStatus.ACCEPTED)){
                    if(statusPlace == "Open"){
                        status.setTextColor(ContextCompat.getColor(itemView.context, R.color.green))
                        status.text = "Abierto"
                    } else {
                        status.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
                        status.text = "Cerrado"
                    }
                    ratingX.text = rating.toString()
                }else if(place_AorR.equals(PlaceStatus.PENDING)){
                    status.setTextColor(ContextCompat.getColor(itemView.context, R.color.orange))
                    status.text = "EN REVISION"
                    ratingX.text = "Sin calificación"
                }else{
                    status.setTextColor(ContextCompat.getColor(itemView.context, R.color.red))
                    status.text = "DENEGADO (ADMIN)"
                    ratingX.text = "Sin calificación"
                }
            }
            ratingX.gravity = Gravity.CENTER
            status.gravity = Gravity.CENTER
            codePlace = place.key

            //icon.text = Categories.get(place.id)!!.icon

            Firebase.firestore
                .collection("categories")
                .whereEqualTo("id", place.key)
                .get()
                .addOnSuccessListener {
                    for(doc in it){
                        icon.text = doc.toObject(Category::class.java).icon
                    }


                }
                .addOnFailureListener {
                }
        }

        override fun onClick(p0: View?) {
            val intent = Intent(name.context, DetailPlaceActivity::class.java)
            Log.e("Place Adapder", "Código enviado: ${codePlace}")
            intent.putExtra("code", codePlace)
            name.context.startActivity(intent)
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        google_map = googleMap
        google_map.moveCamera( CameraUpdateFactory.newLatLngZoom(defaultLocation, 12f))
    }
}