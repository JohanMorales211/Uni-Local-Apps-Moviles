package com.example.unilocal.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.unilocal.R
import com.example.unilocal.db.Users
import com.example.unilocal.model.Comment
import com.example.unilocal.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class CommentsAdapter(var list:ArrayList<Comment>): RecyclerView.Adapter<CommentsAdapter.ViewHolder>() {

    lateinit var context:Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.comment_place, parent, false)
        context = parent.context
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(var commentsPlace: View):RecyclerView.ViewHolder(commentsPlace), OnClickListener{

        val owner:TextView = commentsPlace.findViewById(R.id.owner_name)
        val date:TextView = commentsPlace.findViewById(R.id.date)
        val commentX:TextView = commentsPlace.findViewById(R.id.comment)
        val userImg:ImageView = commentsPlace.findViewById(R.id.user_image_comment)

        init {
            commentsPlace.setOnClickListener(this)
        }

        fun bind(comment: Comment) {
            val formatoFecha = SimpleDateFormat("dd/MM/yyyy")
            Firebase.firestore
                .collection("users")
                .document(comment.idUser)
                .get()
                .addOnSuccessListener {
                    var user = it.toObject(User::class.java)
                    if (user != null) {
                        owner.text = user.name
                        Glide.with(context)
                            .load(user.imgUrl)
                            .into(userImg)
                    }
                    commentX.text = comment.text
                    date.text = formatoFecha.format(comment.date)

                }
                .addOnFailureListener{

                }
            //Users.findNameByID(comment.idUser)

        }

        override fun onClick(p0: View?) {

        }



    }
}