package com.example.unilocal.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.unilocal.adapter.ImagePagerAdapter
import com.example.unilocal.adapter.PlaceModeratorAdapter
import com.example.unilocal.databinding.FragmentAcceptedPlacesBinding
import com.example.unilocal.model.Place
import com.example.unilocal.model.PlaceStatus
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AcceptedPlacesFragment : Fragment() {

    lateinit var  binding: FragmentAcceptedPlacesBinding
    lateinit var listAcceptedPlaces:ArrayList<Place>
    lateinit var adapter:PlaceModeratorAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAcceptedPlacesBinding.inflate(inflater, container, false)

        listAcceptedPlaces  = ArrayList()

        adapter = PlaceModeratorAdapter(listAcceptedPlaces)
        binding.listPlaces.adapter = adapter
        binding.listPlaces.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        Firebase.firestore
            .collection("places")
            .whereEqualTo("status", PlaceStatus.ACCEPTED)
            .get()
            .addOnSuccessListener {
                if(it.isEmpty){
                    //AQUI MOSTRAR MENSAJE DE QUE NO HAY LUGARES CREADOS
                }else{
                    for(doc in it){
                        val place = doc.toObject(Place::class.java)
                        if(place != null){
                            place.key = doc.id
                            listAcceptedPlaces.add(place)
                            adapter.notifyItemInserted(listAcceptedPlaces.size-1)
                        }
                    }
                }
            }

        return binding.root
    }
    override fun onResume() {
        super.onResume()
        //listAcceptedPlaces = Places.ListByState(PlaceStatus.ACCEPTED)
        Firebase.firestore
            .collection("places")
            .whereEqualTo("status", PlaceStatus.ACCEPTED)
            .get()
            .addOnSuccessListener {
                    for(doc in it){
                        val place = doc.toObject(Place::class.java)
                        if(place != null){

                        }
                    }
            }
        adapter = PlaceModeratorAdapter(listAcceptedPlaces)
        binding.listPlaces.adapter = adapter
        binding.listPlaces.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        Log.e("AcceptedPlacesFragment", "Se actualizo la lista")
    }




}