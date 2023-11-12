package com.example.unilocal.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unilocal.R
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.databinding.FragmentAcceptedPlacesBinding
import com.example.unilocal.databinding.FragmentAllAcceptedPlacesBinding
import com.example.unilocal.db.Places
import com.example.unilocal.model.Place
import com.example.unilocal.model.PlaceStatus
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    lateinit var  binding: FragmentAllAcceptedPlacesBinding
    lateinit var listAcceptedPlaces:ArrayList<Place>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllAcceptedPlacesBinding.inflate(inflater, container, false)

        listAcceptedPlaces = ArrayList()

        val adapter = PlaceAdapter(listAcceptedPlaces)
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
        //return inflater.inflate(R.layout.fragment_home, container, false)
    }
}