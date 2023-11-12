package com.example.unilocal.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unilocal.R
import com.example.unilocal.adapter.CommentsAdapter
import com.example.unilocal.databinding.FragmentCommentsPlaceBinding
import com.example.unilocal.db.Comments
import com.example.unilocal.model.Comment
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class CommentsPlaceFragment : Fragment() {

    lateinit var binding: FragmentCommentsPlaceBinding
    lateinit var list:ArrayList<Comment>
    var codePlace:String = ""
    private lateinit var adapter: CommentsAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null){
            codePlace = requireArguments().getString("id_place", "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommentsPlaceBinding.inflate(inflater, container, false)

        list = ArrayList()
        adapter = CommentsAdapter(list)
        binding.listComments.adapter = adapter
        binding.listComments.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        Firebase.firestore
            .collection("places")
            .document(codePlace)
            .collection("comments")
            .get()
            .addOnSuccessListener {
                if(it.isEmpty){
                    //AQUI MOSTRAR MENSAJE DE QUE NO HAY COMMENTS
                }else{
                    for(doc in it){
                        val comment = doc.toObject(Comment::class.java)
                        if(comment != null){
                            comment.key = doc.id
                            list.add(comment)
                            adapter.notifyItemInserted(list.size-1)
                        }
                    }
                }
            }
            .addOnFailureListener{
                Snackbar.make(binding.root, "${it.message}", Snackbar.LENGTH_LONG).show()
            }



        binding.sendButton.setOnClickListener { createComment() }


        return binding.root
    }

    fun createComment(){
        val text = binding.messageInput.text.toString()

        if(text.isNotEmpty()){

            val user = FirebaseAuth.getInstance()
            if(user!=null){
                val comment = Comment(text, user.uid!!, 1)

                Firebase.firestore
                    .collection("places")
                    .document(codePlace)
                    .collection("comments")
                    .add(comment)
                    .addOnSuccessListener {
                        list.add(comment)
                        adapter.notifyItemInserted(list.size-1)
                        Snackbar.make(binding.root, "Mensaje Enviado", Snackbar.LENGTH_LONG).show()
                    }
                    .addOnFailureListener(){
                        Snackbar.make(binding.root, "${it.message}", Snackbar.LENGTH_LONG).show()
                    }
                clearInputs()
            }

        } else {
            Snackbar.make(binding.root, "Hay q escribir el mensaje", Snackbar.LENGTH_LONG).show()
        }
    }

    fun clearInputs(){
        binding.messageInput.setText("")
    }

    companion object{
        fun newInstance(codePlace: String):CommentsPlaceFragment{
            val args = Bundle()
            args.putString("id_place", codePlace)
            val fragment = CommentsPlaceFragment()
            fragment.arguments = args
            return fragment
        }
    }
}