package com.example.unilocal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unilocal.R
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.databinding.ActivitySearchResultBinding
import com.example.unilocal.db.Places
import com.example.unilocal.model.Place
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SearchResultActivity : AppCompatActivity() {

    lateinit var binding:ActivitySearchResultBinding
    var searchText:String = ""
    lateinit var listPlaces:ArrayList<Place>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchText = intent.extras!!.getString("text", "")
        listPlaces = ArrayList()

        val adapter = PlaceAdapter(listPlaces)
        binding.listPlaces.adapter = adapter
        binding.listPlaces.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        Log.e("Texto busqueda", searchText)

        if(searchText.isNotEmpty()){
            //listPlaces = Places.findByName(searchText)
            Firebase.firestore
                .collection("places")
                .whereEqualTo("name", searchText)
                .get()
                .addOnSuccessListener{
                    for(doc in it){
                        var place = doc.toObject(Place::class.java)
                        listPlaces.add(place)
                        adapter.notifyDataSetChanged()
                    }
                }

        }

    }
}