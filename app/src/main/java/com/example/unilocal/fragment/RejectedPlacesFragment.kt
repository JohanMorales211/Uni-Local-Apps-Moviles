package com.example.unilocal.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unilocal.R
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.adapter.PlaceModeratorAdapter
import com.example.unilocal.databinding.FragmentRejectedPlacesBinding
import com.example.unilocal.db.Places
import com.example.unilocal.model.Place
import com.example.unilocal.model.PlaceStatus
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RejectedPlacesFragment : Fragment() {

    lateinit var binding: FragmentRejectedPlacesBinding
    lateinit var listRejectedPlaces:ArrayList<Place>
    lateinit var adapter:PlaceModeratorAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRejectedPlacesBinding.inflate(inflater, container, false)

        listRejectedPlaces = ArrayList()//Places.ListByState(PlaceStatus.REJECTED)

        adapter = PlaceModeratorAdapter(listRejectedPlaces)
        binding.listPlaces.adapter = adapter
        binding.listPlaces.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        Firebase.firestore
            .collection("places")
            .whereEqualTo("status", PlaceStatus.REJECTED)
            .get()
            .addOnSuccessListener {
                if(it.isEmpty){
                    //AQUI MOSTRAR MENSAJE DE QUE NO HAY LUGARES CREADOS
                }else{
                    for(doc in it){
                        val place = doc.toObject(Place::class.java)
                        if(place != null){
                            place.key = doc.id
                            listRejectedPlaces.add(place)
                            adapter.notifyItemInserted(listRejectedPlaces.size-1)
                        }
                    }
                }
            }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        //listRejectedPlaces = Places.ListByState(PlaceStatus.REJECTED)

        Firebase.firestore
            .collection("places")
            .whereEqualTo("status", PlaceStatus.REJECTED)
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    val place = doc.toObject(Place::class.java)
                    if(place != null){
                        /*listRejectedPlaces.clear()
                        listRejectedPlaces.add(place)
                        adapter.notifyItemInserted(listRejectedPlaces.size-1)*/
                    }
                }
            }
        adapter = PlaceModeratorAdapter(listRejectedPlaces)
        binding.listPlaces.adapter = adapter
        binding.listPlaces.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        Log.e("RejectedPlacesFragment", "Se actualizo la lista")
    }
}