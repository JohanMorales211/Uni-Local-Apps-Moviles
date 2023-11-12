package com.example.unilocal.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unilocal.R
import com.example.unilocal.adapter.PlaceAdapter
import com.example.unilocal.adapter.PlaceModeratorAdapter
import com.example.unilocal.databinding.FragmentPendingPlacesBinding
import com.example.unilocal.db.Places
import com.example.unilocal.model.Place
import com.example.unilocal.model.PlaceStatus
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class PendingPlacesFragment : Fragment() {

    lateinit var binding:FragmentPendingPlacesBinding
    lateinit var listPendingPlaces:ArrayList<Place>
    lateinit var adapter:PlaceModeratorAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPendingPlacesBinding.inflate(inflater, container, false)

        listPendingPlaces = ArrayList()//Places.ListByState(PlaceStatus.PENDING)

        adapter = PlaceModeratorAdapter(listPendingPlaces)
        binding.listPlaces.adapter = adapter
        binding.listPlaces.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        Firebase.firestore
            .collection("places")
            .whereEqualTo("status", PlaceStatus.PENDING)
            .get()
            .addOnSuccessListener {
                if(it.isEmpty){
                    //AQUI MOSTRAR MENSAJE DE QUE NO HAY LUGARES CREADOS
                }else{
                    for(doc in it){
                        val place = doc.toObject(Place::class.java)
                        if(place != null){
                            place.key = doc.id
                            listPendingPlaces.add(place)
                            adapter.notifyItemInserted(listPendingPlaces.size-1)
                        }
                    }
                }
            }

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        //listPendingPlaces = Places.ListByState(PlaceStatus.PENDING)
        Firebase.firestore
            .collection("places")
            .whereEqualTo("status", PlaceStatus.PENDING)
            .get()
            .addOnSuccessListener {
                for(doc in it){
                    val place = doc.toObject(Place::class.java)
                    if(place != null){
                        /*listPendingPlaces.clear()
                        listPendingPlaces.add(place)
                        adapter.notifyItemInserted(listPendingPlaces.size-1)*/
                    }
                }
            }
        adapter = PlaceModeratorAdapter(listPendingPlaces)
        binding.listPlaces.adapter = adapter
        binding.listPlaces.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        Log.e("PendingPlacesFragment", "Se actualizo la lista")
    }
}